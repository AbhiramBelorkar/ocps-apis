package com.ocps.auth.enums;

public enum ClaimType {

    FORM_19("01"),
    FORM_31("02"),
    FORM_20("03"),
    FORM_10C("04"),
    FORM_13("05");

    private final String code;

    ClaimType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
