package com.ocps.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ocps.auth.enums.ClaimStatus;
import com.ocps.auth.enums.ClaimType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "claim")
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tracking_id", unique = true)
    private String trackingId;

    @Enumerated(EnumType.STRING)
    @Column(name = "claim_type")
    private ClaimType claimType;

    @Enumerated(EnumType.STRING)
    @Column(name = "claim_status")
    private ClaimStatus claimStatus;

    @Column(name = "requested_amount")
    private BigDecimal requestedAmount;

    @Column(name = "eligible_amount")
    private BigDecimal eligibleAmount;

    @Column(name = "submission_time")
    private LocalDateTime submissionTime;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "uan_id")
    private UanMaster uanMaster;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "member_account_id")
    private MemberAccount memberAccount;

    public Claim() {
    }

    public Long getId() {
        return id;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public ClaimType getClaimType() {
        return claimType;
    }

    public void setClaimType(ClaimType claimType) {
        this.claimType = claimType;
    }

    public ClaimStatus getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(ClaimStatus claimStatus) {
        this.claimStatus = claimStatus;
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

    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(LocalDateTime submissionTime) {
        this.submissionTime = submissionTime;
    }

    public UanMaster getUanMaster() {
        return uanMaster;
    }

    public void setUanMaster(UanMaster uanMaster) {
        this.uanMaster = uanMaster;
    }

    public MemberAccount getMemberAccount() {
        return memberAccount;
    }

    public void setMemberAccount(MemberAccount memberAccount) {
        this.memberAccount = memberAccount;
    }
}