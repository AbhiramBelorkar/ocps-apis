package com.ocps.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ClaimRequestDto {

    @NotBlank(message = "UAN is required")
    private String uan;

    @NotBlank(message = "Member ID is required")
    private String memberId;

    @NotBlank(message = "Claim Type is required")
    private String claimType;

    @NotNull(message = "Requested amount is required")
    private BigDecimal requestedAmount;

    public ClaimRequestDto() {
    }

    public String getUan() {
        return uan;
    }

    public void setUan(String uan) {
        this.uan = uan;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(BigDecimal requestedAmount) {
        this.requestedAmount = requestedAmount;
    }
}