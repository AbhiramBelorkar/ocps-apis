package com.ocps.claim.entity;

import com.ocps.claim.enums.ClaimStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "claim_status_history")
public class ClaimStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "claim_status")
    private ClaimStatus claimStatus;

    @Column(name = "action_by")
    private String actionBy;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "action_time")
    private LocalDateTime actionTime;

    @ManyToOne
    @JoinColumn(name = "claim_id")
    private Claim claim;

    public ClaimStatusHistory() {
    }

    public Long getId() {
        return id;
    }

    public ClaimStatus getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(ClaimStatus claimStatus) {
        this.claimStatus = claimStatus;
    }

    public String getActionBy() {
        return actionBy;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getActionTime() {
        return actionTime;
    }

    public void setActionTime(LocalDateTime actionTime) {
        this.actionTime = actionTime;
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }
}