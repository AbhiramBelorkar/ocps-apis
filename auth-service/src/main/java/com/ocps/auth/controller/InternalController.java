package com.ocps.auth.controller;

import com.ocps.auth.dto.MemberDetailsResponseDto;
import com.ocps.auth.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
public class InternalController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/member/{uan}")
    public MemberDetailsResponseDto getMemberDetails(@PathVariable String uan) {
        return authenticationService.getMemberDetails(uan);
    }
}