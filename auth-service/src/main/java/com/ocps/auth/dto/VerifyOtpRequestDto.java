package com.ocps.auth.dto;

public class VerifyOtpRequestDto {

    private String uan;

    private String otp;

    public VerifyOtpRequestDto() {
    }

    public String getUan() {
        return uan;
    }

    public void setUan(String uan) {
        this.uan = uan;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}