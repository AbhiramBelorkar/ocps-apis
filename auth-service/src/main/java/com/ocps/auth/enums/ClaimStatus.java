package com.ocps.auth.enums;

public enum ClaimStatus {

    MEMBER_SUBMITTED("MS"),
    PENDING_DA("PD"),
    PENDING_SS("PS"),
    PENDING_AO("PA"),
    PENDING_APFC("PP"),
    REJECTED("RJ"),
    SETTLED("ST"),
    AUTO_PROCESSED("AP");

    private final String code;

    ClaimStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
