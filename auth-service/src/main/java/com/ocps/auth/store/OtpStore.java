package com.ocps.auth.store;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OtpStore {

    private final Map<String, String> otpMap = new HashMap<>();

    public void saveOtp(String uan, String otp) {
        otpMap.put(uan, otp);
    }

    public String getOtp(String uan) {
        return otpMap.get(uan);
    }

    public void removeOtp(String uan) {
        otpMap.remove(uan);
    }
}