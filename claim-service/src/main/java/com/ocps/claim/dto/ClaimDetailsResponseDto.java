package com.ocps.claim.dto;

import com.ocps.claim.dto.ClaimHistoryDto;

import java.math.BigDecimal;
import java.util.List;

public class ClaimDetailsResponseDto {

    private String trackingId;

    private String claimType;

    private String status;

    private BigDecimal requestedAmount;

    private BigDecimal eligibleAmount;

    private List<ClaimHistoryDto> history;

    public ClaimDetailsResponseDto() {
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(BigDecimal requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public BigDecimal getEligibleAmount() {
        return eligibleAmount;
    }

    public void setEligibleAmount(BigDecimal eligibleAmount) {
        this.eligibleAmount = eligibleAmount;
    }

    public List<ClaimHistoryDto> getHistory() {
        return history;
    }

    public void setHistory(List<ClaimHistoryDto> history) {
        this.history = history;
    }
}