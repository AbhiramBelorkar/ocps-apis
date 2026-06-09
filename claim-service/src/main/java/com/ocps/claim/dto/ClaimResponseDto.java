package com.ocps.claim.dto;

public class ClaimResponseDto {

    private String trackingId;

    private String status;

    private String message;

    public ClaimResponseDto() {
    }

    public ClaimResponseDto(
            String trackingId,
            String status,
            String message) {

        this.trackingId = trackingId;
        this.status = status;
        this.message = message;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}