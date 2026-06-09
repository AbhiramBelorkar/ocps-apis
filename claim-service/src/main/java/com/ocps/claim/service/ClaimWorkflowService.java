package com.ocps.claim.service;

import com.ocps.claim.enums.ClaimAction;
import com.ocps.claim.enums.ClaimStatus;
import com.ocps.claim.exception.InvalidWorkflowException;
import com.ocps.claim.exception.UnauthorizedActionException;
import org.springframework.stereotype.Service;

@Service
public class ClaimWorkflowService {

    public ClaimStatus determineNextStatus(ClaimStatus currentStatus, String actor, ClaimAction action) {
        // MEMBER_SUBMITTED -> PENDING_DA
        if(currentStatus == ClaimStatus.MEMBER_SUBMITTED) {
            if(!actor.equals("DA")) {
                throw new InvalidWorkflowException("Only DA can process MEMBER_SUBMITTED claims");
            }
            if(action == ClaimAction.RECOMMEND_APPROVE) {
                return ClaimStatus.PENDING_SS;
            }
            if(action == ClaimAction.RECOMMEND_REJECT) {
                return ClaimStatus.PENDING_APFC;
            }
            throw new InvalidWorkflowException("Invalid action for DA");
        }
        // DA Actions
        if(actor.equals("DA")) {
            if(currentStatus != ClaimStatus.PENDING_DA) {
                throw new InvalidWorkflowException("DA can process only PENDING_DA claims");
            }
            if(action == ClaimAction.RECOMMEND_APPROVE) {
                return ClaimStatus.PENDING_SS;
            }
            if(action == ClaimAction.RECOMMEND_REJECT) {
                return ClaimStatus.PENDING_APFC;
            }
            throw new InvalidWorkflowException("Invalid action for DA");
        }
        // SS Actions
        if(actor.equals("SS")) {
            if(currentStatus != ClaimStatus.PENDING_SS) {
                throw new InvalidWorkflowException("SS can process only PENDING_SS claims");
            }
            if(action == ClaimAction.APPROVE) {
                return ClaimStatus.PENDING_AO;
            }
            if(action == ClaimAction.SEND_BACK) {
                return ClaimStatus.PENDING_DA;
            }
            throw new InvalidWorkflowException("Invalid action for SS");
        }
        // AO Actions
        if(actor.equals("AO")) {
            if(currentStatus != ClaimStatus.PENDING_AO) {
                throw new InvalidWorkflowException("AO can process only PENDING_AO claims");
            }
            if(action == ClaimAction.APPROVE) {
                return ClaimStatus.PENDING_APFC;
            }
            if(action == ClaimAction.SEND_BACK) {
                return ClaimStatus.PENDING_DA;
            }
            throw new InvalidWorkflowException("Invalid action for AO");
        }
        // APFC Actions
        if(actor.equals("APFC")) {
            if(currentStatus != ClaimStatus.PENDING_APFC) {
                throw new InvalidWorkflowException("APFC can process only PENDING_APFC claims");
            }
            if(action == ClaimAction.APPROVE) {
                return ClaimStatus.SETTLED;
            }
            if(action == ClaimAction.REJECT) {
                return ClaimStatus.REJECTED;
            }
            if(action == ClaimAction.SEND_BACK) {
                return ClaimStatus.PENDING_DA;
            }
            throw new InvalidWorkflowException("Invalid action for APFC");
        }
        throw new UnauthorizedActionException("Invalid Actor");
    }

    public void validateActor(ClaimStatus currentStatus, String actor) {
        switch (currentStatus) {
            case MEMBER_SUBMITTED -> {
                // Since auto processing is skipped for now,
                // DA will directly process submitted claims
                if (!actor.equals("DA")) {
                    throw new InvalidWorkflowException("Only DA can process MEMBER_SUBMITTED");
                }
            }
            case PENDING_DA -> {
                if (!actor.equals("DA")) {
                    throw new InvalidWorkflowException("Only DA can process PENDING_DA");
                }
            }
            case PENDING_SS -> {
                if (!actor.equals("SS")) {
                    throw new InvalidWorkflowException("Only SS can process PENDING_SS");
                }
            }
            case PENDING_AO -> {
                if (!actor.equals("AO")) {
                    throw new InvalidWorkflowException("Only AO can process PENDING_AO");
                }
            }
            case PENDING_APFC -> {
                if (!actor.equals("APFC")) {
                    throw new InvalidWorkflowException("Only APFC can process PENDING_APFC");
                }
            }
            default -> throw new InvalidWorkflowException("Invalid Claim Status");
        }
    }

}
