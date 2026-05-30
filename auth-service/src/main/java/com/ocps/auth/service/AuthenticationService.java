package com.ocps.auth.service;

import com.ocps.auth.client.NotificationClient;
import com.ocps.auth.dto.*;
import com.ocps.auth.entity.*;
import com.ocps.auth.enums.NotificationType;
import com.ocps.auth.event.NotificationEvent;
import com.ocps.auth.event.NotificationProducer;
import com.ocps.auth.repository.OfficeUserRepository;
import com.ocps.auth.repository.UanMasterRepository;
import com.ocps.auth.store.OtpStore;
import com.ocps.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private UanMasterRepository uanMasterRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private OfficeUserRepository officeUserRepository;
    @Autowired
    private NotificationClient notificationClient;
    @Autowired
    private OtpStore otpStore;
    @Autowired
    private NotificationProducer notificationProducer;

    public LoginResponseDto memberLogin(LoginRequestDto loginRequestDto) {
        Optional<UanMaster> optionalUanMaster = uanMasterRepository.findByUan(loginRequestDto.getUan());
        if (optionalUanMaster.isEmpty()) {
            return new LoginResponseDto("Invalid UAN");
        }

        UanMaster uanMaster = optionalUanMaster.get();
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), uanMaster.getPassword())) {
//        if (!loginRequestDto.getPassword().equals(uanMaster.getPassword())){
            return new LoginResponseDto("Invalid Password");
        }

        String otp = generateOtp();
        otpStore.saveOtp(uanMaster.getUan(), otp);

        NotificationEvent event = new NotificationEvent();
        event.setRecipient(uanMaster.getUan());
        event.setMessage("Your OTP is: " + otp);
        event.setType(NotificationType.OTP.name());
        notificationProducer.send(event);

        return new LoginResponseDto("OTP Sent");
    }

    public LoginResponseDto officeLogin(OfficeLoginRequestDto requestDto) {
        OfficeUser officeUser = officeUserRepository
                .findByUserId(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Invalid Username"));
        if (!passwordEncoder.matches(requestDto.getPassword(), officeUser.getPassword())){
//        if (!requestDto.getPassword().equals(officeUser.getPassword())) {
            return new LoginResponseDto("Invalid Password");
        }
        String token = jwtUtil.generateToken(officeUser.getUsername(), officeUser.getRole());
        return new LoginResponseDto("Login Successful", token);
    }

    private String generateOtp() {
        int otp = (int) ((Math.random() * 900000) + 100000);
        return String.valueOf(otp);
    }

    public LoginResponseDto verifyOtp(VerifyOtpRequestDto requestDto) {
        String storedOtp = otpStore.getOtp(requestDto.getUan());

        if (storedOtp == null) {
            return new LoginResponseDto("OTP Expired");
        }

        if (!storedOtp.equals(requestDto.getOtp())) {
            return new LoginResponseDto("Invalid OTP");
        }

        otpStore.removeOtp(requestDto.getUan());
        String token = jwtUtil.generateToken(
                        requestDto.getUan(),
                        "MEMBER");

        return new LoginResponseDto("Login Successful", token);
    }

}