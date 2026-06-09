package com.ocps.claim.controller;

import com.ocps.claim.dto.ClaimActionRequestDto;
import com.ocps.claim.entity.Claim;
import com.ocps.claim.service.ClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workflow")
public class WorkflowController {

    @Autowired
    private ClaimService claimService;

    @PostMapping("/action")
    public String processClaimAction(@RequestBody ClaimActionRequestDto requestDto) {
        return claimService.processClaimAction(requestDto);
    }

    @GetMapping("/pending")
    public List<Claim> getPendingClaims() {
        return claimService.getPendingClaims();
    }
}
