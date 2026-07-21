package com.ocps.claim.service;

import com.ocps.claim.client.AuthClient;
import com.ocps.claim.dto.*;
import com.ocps.claim.entity.Claim;
import com.ocps.claim.entity.ClaimStatusHistory;
import com.ocps.claim.enums.ClaimStatus;
import com.ocps.claim.enums.ClaimType;
import com.ocps.claim.event.ClaimEventProducer;
import com.ocps.claim.event.ClaimSubmittedEvent;
import com.ocps.claim.exception.ClaimNotFoundException;
import com.ocps.claim.exception.UnauthorizedActionException;
import com.ocps.claim.repository.ClaimRepository;
import com.ocps.claim.repository.ClaimStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClaimService {
    
    @Autowired
    private ClaimRepository claimRepository;
    @Autowired
    private ClaimStatusHistoryRepository claimStatusHistoryRepository;
    @Autowired
    private ClaimWorkflowService claimWorkflowService;
    @Autowired
    private AuthClient authClient;
    @Autowired
    private ClaimEventProducer claimEventProducer;

    @Transactional
    public ClaimResponseDto submitClaim(ClaimRequestDto requestDto) {
        MemberDetailsResponseDto member = authClient.getMemberDetails(requestDto.getUan());

        if (member == null) {
            throw new RuntimeException("Invalid UAN");
        }

        boolean memberExists = member.getMemberAccounts()
                .stream()
                .anyMatch(account -> account.getMemberId()
                                .equals(requestDto.getMemberId()));

        if (!memberExists) {
            throw new RuntimeException("Member ID Not Found");
        }

        Claim claim = new Claim();
        ClaimType claimType = mapClaimType(requestDto.getClaimType());
        claim.setTrackingId(generateTrackingId(requestDto.getUan(), claimType));
        claim.setClaimType(claimType);
        claim.setClaimStatus(ClaimStatus.MEMBER_SUBMITTED);
        claim.setRequestedAmount(requestDto.getRequestedAmount());
        claim.setEligibleAmount(calculateEligibleAmount(claimType));
        claim.setSubmissionTime(LocalDateTime.now());
        claim.setUan(requestDto.getUan());
        claim.setMemberId(requestDto.getMemberId());
        claimRepository.save(claim);

        ClaimSubmittedEvent event = new ClaimSubmittedEvent();
        event.setTrackingId(claim.getTrackingId());
        event.setUan(claim.getUan());
        event.setMemberId(claim.getMemberId());
        event.setStatus(claim.getClaimStatus().name());
        claimEventProducer.publishClaimSubmitted(event);

        ClaimStatusHistory history = new ClaimStatusHistory();
        history.setClaim(claim);
        history.setClaimStatus(ClaimStatus.MEMBER_SUBMITTED);
        history.setActionBy("MEMBER");
        history.setRemarks("Claim submitted by member");
        history.setActionTime(LocalDateTime.now());
        claimStatusHistoryRepository.save(history);

        return new ClaimResponseDto(claim.getTrackingId(),
                claim.getClaimStatus().getCode(),
                "Claim Submitted Successfully"
        );
    }

    private String generateTrackingId(String uan, ClaimType claimType) {
        long count = claimRepository.countByUanAndClaimType(uan, claimType);
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        String actor = authentication.getAuthorities()
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("No role assigned"))
                .getAuthority()
                .replace("ROLE_", "");

        Claim claim = claimRepository.findByTrackingId(requestDto.getTrackingId())
                .orElseThrow(() -> new ClaimNotFoundException("Claim Not Found"));
        claimWorkflowService.validateActor(claim.getClaimStatus(), actor);

        ClaimStatus nextStatus = claimWorkflowService.determineNextStatus(
                        claim.getClaimStatus(),
                        actor,
                        requestDto.getAction());
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
        List<Claim> claims = claimRepository.findByUan(uan);
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
