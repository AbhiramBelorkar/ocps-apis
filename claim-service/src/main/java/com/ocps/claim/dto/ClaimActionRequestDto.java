package com.ocps.claim.dto;

import com.ocps.claim.enums.ClaimAction;

public class ClaimActionRequestDto {

    private String trackingId;

    private ClaimAction action;

    private String remarks;

    public ClaimActionRequestDto() {
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public ClaimAction getAction() {
        return action;
    }

    public void setAction(ClaimAction action) {
        this.action = action;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}