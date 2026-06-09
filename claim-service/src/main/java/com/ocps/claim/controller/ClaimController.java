package com.ocps.claim.controller;

import com.ocps.claim.dto.ClaimDetailsResponseDto;
import com.ocps.claim.dto.ClaimRequestDto;
import com.ocps.claim.dto.ClaimResponseDto;
import com.ocps.claim.entity.Claim;
import com.ocps.claim.service.ClaimService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/claim")
public class ClaimController {

    @Autowired
    private ClaimService claimService;

    @GetMapping("/test")
    public String test() {
        return "WORKING";
    }

    @PostMapping("/submit")
    public ClaimResponseDto submitClaim(@Valid @RequestBody ClaimRequestDto requestDto) {
        return claimService.submitClaim(requestDto);
    }

    @GetMapping("/details/{trackingId}")
    public ClaimDetailsResponseDto getClaimDetails(@PathVariable String trackingId) {
        return claimService.getClaimDetails(trackingId);
    }

    @GetMapping("/pending")
    public List<Claim> getInboxClaims() {
        return claimService.getPendingClaims();
    }

    @GetMapping("/my-claims")
    public List<ClaimDetailsResponseDto> getMyClaims() {
        return claimService.getMyClaims();
    }

}
