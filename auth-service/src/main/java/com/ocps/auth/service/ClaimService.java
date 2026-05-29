package com.ocps.auth.service;

import com.ocps.auth.dto.*;
import com.ocps.auth.entity.Claim;
import com.ocps.auth.entity.ClaimStatusHistory;
import com.ocps.auth.entity.MemberAccount;
import com.ocps.auth.entity.UanMaster;
import com.ocps.auth.enums.ClaimStatus;
import com.ocps.auth.enums.ClaimType;
import com.ocps.auth.exception.ClaimNotFoundException;
import com.ocps.auth.exception.UnauthorizedActionException;
import com.ocps.auth.repository.ClaimRepository;
import com.ocps.auth.repository.ClaimStatusHistoryRepository;
import com.ocps.auth.repository.UanMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClaimService {

    @Autowired
    private UanMasterRepository uanMasterRepository;
    @Autowired
    private ClaimRepository claimRepository;
    @Autowired
    private ClaimStatusHistoryRepository claimStatusHistoryRepository;
    @Autowired
    private ClaimWorkflowService claimWorkflowService;

    public MemberDetailsResponseDto getMemberDetails(String uan) {
        Optional<UanMaster> optionalUanMaster = uanMasterRepository.findByUan(uan);
        if (optionalUanMaster.isEmpty()) {
            throw new RuntimeException("UAN Not Found");
        }
        UanMaster uanMaster = optionalUanMaster.get();
        MemberDetailsResponseDto responseDto = new MemberDetailsResponseDto();
        responseDto.setUan(uanMaster.getUan());
        responseDto.setMemberName(uanMaster.getMemberName());

        List<MemberAccountDto> memberAccountDtos = uanMaster.getMemberAccounts()
                .stream()
                .map(memberAccount -> {
                    MemberAccountDto dto = new MemberAccountDto();
                    dto.setMemberId(memberAccount.getMemberId());
                    dto.setEstablishmentName(memberAccount.getEstablishmentName());
                    dto.setDoj(memberAccount.getDoj());
                    dto.setDoe(memberAccount.getDoe());
                    return dto;
                })
                .toList();
        responseDto.setMemberAccounts(memberAccountDtos);
        return responseDto;
    }

    @Transactional
    public ClaimResponseDto submitClaim(ClaimRequestDto requestDto) {
        UanMaster uanMaster = uanMasterRepository.findByUan(requestDto.getUan())
                .orElseThrow(() -> new RuntimeException("UAN Not Found"));

        MemberAccount memberAccount = uanMaster.getMemberAccounts()
                .stream()
                .filter(account -> account.getMemberId()
                        .equals(requestDto.getMemberId()))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Member ID Not Found"));

        Claim claim = new Claim();
        ClaimType claimType = mapClaimType(requestDto.getClaimType());
        claim.setTrackingId(generateTrackingId(requestDto.getUan(), claimType));
        claim.setClaimType(claimType);
        claim.setClaimStatus(ClaimStatus.MEMBER_SUBMITTED);
        claim.setRequestedAmount(requestDto.getRequestedAmount());
        BigDecimal eligibleAmount = calculateEligibleAmount(claimType);
        claim.setEligibleAmount(eligibleAmount);
        claim.setSubmissionTime(LocalDateTime.now());
        claim.setUanMaster(uanMaster);
        claim.setMemberAccount(memberAccount);
        claimRepository.save(claim);

        ClaimStatusHistory history = new ClaimStatusHistory();
        history.setClaim(claim);
        history.setClaimStatus(ClaimStatus.MEMBER_SUBMITTED);
        history.setActionBy("MEMBER");
        history.setRemarks("Claim submitted by member");
        history.setActionTime(LocalDateTime.now());
        claimStatusHistoryRepository.save(history);

        return new ClaimResponseDto(
                claim.getTrackingId(),
                claim.getClaimStatus().getCode(),
                "Claim Submitted Successfully"
        );
    }

    private String generateTrackingId(String uan, ClaimType claimType) {
        long count = claimRepository.countByUanMaster_UanAndClaimType(uan, claimType);
        long nextSequence = count + 1;
        return uan + claimType.getCode() + String.format("%03d", nextSequence);
    }

    private ClaimType mapClaimType(String code) {
        return switch (code) {
            case "01" -> ClaimType.FORM_19;
            case "02" -> ClaimType.FORM_31;
            case "03" -> ClaimType.FORM_20;
            case "04" -> ClaimType.FORM_10C;
            case "05" -> ClaimType.FORM_13;
            default -> throw new RuntimeException(
                    "Invalid Claim Type");
        };
    }

    private BigDecimal calculateEligibleAmount(
            ClaimType claimType) {
        return switch (claimType) {
            case FORM_19 -> BigDecimal.valueOf(50000);
            case FORM_31 -> BigDecimal.valueOf(30000);
            case FORM_10C -> BigDecimal.valueOf(20000);
            case FORM_20 -> BigDecimal.valueOf(100000);
            case FORM_13 -> BigDecimal.ZERO;
        };
    }

    public ClaimDetailsResponseDto getClaimDetails(String trackingId) {
        Claim claim = claimRepository.findByTrackingId(trackingId).orElseThrow(() -> new RuntimeException("Claim Not Found"));
        List<ClaimStatusHistory> historyList = claimStatusHistoryRepository.findByClaimOrderByActionTimeAsc(claim);
        List<ClaimHistoryDto> historyDtos = historyList.stream().map(history -> {
            ClaimHistoryDto dto = new ClaimHistoryDto();
            dto.setStatus(history.getClaimStatus().getCode());
            dto.setActionBy(history.getActionBy());
            dto.setRemarks(history.getRemarks());
            dto.setActionTime(history.getActionTime());
            return dto;
        }).toList();
        ClaimDetailsResponseDto response = new ClaimDetailsResponseDto();
        response.setTrackingId(claim.getTrackingId());
        response.setClaimType(claim.getClaimType().name());
        response.setStatus(claim.getClaimStatus().getCode());
        response.setRequestedAmount(claim.getRequestedAmount());
        response.setEligibleAmount(claim.getEligibleAmount());
        response.setHistory(historyDtos);
        return response;
    }

    @Transactional
    public String processClaimAction(ClaimActionRequestDto requestDto) {
        System.out.println(requestDto);
        Claim claim = claimRepository.findByTrackingId(requestDto.getTrackingId())
                .orElseThrow(() -> new ClaimNotFoundException("Claim Not Found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String actor = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority();
        actor = actor.replace("ROLE_", "");

        claimWorkflowService.validateActor(claim.getClaimStatus(), actor);

        ClaimStatus nextStatus = claimWorkflowService.determineNextStatus(claim.getClaimStatus(), actor,requestDto.getAction());
        claim.setClaimStatus(nextStatus);
        claimRepository.save(claim);
        ClaimStatusHistory history = new ClaimStatusHistory();
        history.setClaim(claim);
        history.setClaimStatus(nextStatus);
        history.setActionBy(actor);
        history.setRemarks(requestDto.getRemarks());
        history.setActionTime(LocalDateTime.now());
        claimStatusHistoryRepository.save(history);
        return "Claim moved to " + nextStatus.name();
    }


    public List<Claim> getPendingClaims() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        ClaimStatus status = switch (role) {
            case "ROLE_DA" -> ClaimStatus.PENDING_DA;
            case "ROLE_SS" -> ClaimStatus.PENDING_SS;
            case "ROLE_AO" -> ClaimStatus.PENDING_AO;
            case "ROLE_APFC" -> ClaimStatus.PENDING_APFC;
            default -> throw new UnauthorizedActionException("Invalid Role");
        };

        return claimRepository.findByClaimStatus(status);
    }

    public List<ClaimDetailsResponseDto> getMyClaims() {
        Authentication authentication = SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        String uan = authentication.getName();
        List<Claim> claims = claimRepository.findByUanMaster_Uan(uan);
        return claims.stream()
                .map(this::mapToClaimDetailsResponse)
                .toList();
    }

    private ClaimDetailsResponseDto mapToClaimDetailsResponse(Claim claim) {
        List<ClaimStatusHistory> historyList = claimStatusHistoryRepository
                        .findByClaimOrderByActionTimeAsc(claim);

        List<ClaimHistoryDto> historyDtos = historyList.stream()
                        .map(history -> {
                            ClaimHistoryDto dto = new ClaimHistoryDto();
                            dto.setStatus(history.getClaimStatus().getCode());
                            dto.setActionBy(history.getActionBy());
                            dto.setRemarks(history.getRemarks());
                            dto.setActionTime(history.getActionTime());
                            return dto;
                        }).toList();

        ClaimDetailsResponseDto response = new ClaimDetailsResponseDto();
        response.setTrackingId(claim.getTrackingId());
        response.setClaimType(claim.getClaimType().name());
        response.setStatus(claim.getClaimStatus().getCode());
        response.setRequestedAmount(claim.getRequestedAmount());
        response.setEligibleAmount(claim.getEligibleAmount());
        response.setHistory(historyDtos);
        return response;
    }
}
