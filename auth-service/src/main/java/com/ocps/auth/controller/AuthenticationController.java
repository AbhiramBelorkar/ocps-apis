package com.ocps.auth.controller;

import com.ocps.auth.dto.*;
import com.ocps.auth.entity.Claim;
import com.ocps.auth.service.AuthenticationService;
import com.ocps.auth.service.ClaimService;
import com.ocps.auth.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private ClaimService claimService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        // temporary hardcoded users
        if(request.getUsername().equals("da") && request.getPassword().equals("123")) {
            String token = jwtUtil.generateToken("da", "DA");
            return new LoginResponse(token);
        }
        if(request.getUsername().equals("ss") && request.getPassword().equals("123")) {
            String token = jwtUtil.generateToken("ss", "SS");
            return new LoginResponse(token);
        }
        throw new RuntimeException("Invalid Credentials");
    }

    @PostMapping("/office/login")
    public LoginResponseDto officeLogin(@RequestBody OfficeLoginRequestDto requestDto) {
        return authenticationService.officeLogin(requestDto);
    }

    @PostMapping("/member/login")
    public LoginResponseDto memberLogin(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return authenticationService.memberLogin(loginRequestDto);
    }

    @GetMapping("/member/details/{uan}")
    public MemberDetailsResponseDto getMemberDetails( @PathVariable String uan) {
        return claimService.getMemberDetails(uan);
    }

    @PostMapping("/member/verify-otp")
    public LoginResponseDto verifyOtp(@RequestBody VerifyOtpRequestDto requestDto) {
        return authenticationService.verifyOtp(requestDto);
    }

}