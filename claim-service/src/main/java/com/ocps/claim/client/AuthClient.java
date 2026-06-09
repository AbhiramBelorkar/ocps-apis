package com.ocps.claim.client;

import com.ocps.claim.dto.MemberDetailsResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface AuthClient {

    @GetMapping("/internal/member/{uan}")
    MemberDetailsResponseDto getMemberDetails(@PathVariable String uan);
}
