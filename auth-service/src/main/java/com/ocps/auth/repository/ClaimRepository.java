package com.ocps.auth.repository;

import com.ocps.auth.entity.Claim;
import com.ocps.auth.enums.ClaimStatus;
import com.ocps.auth.enums.ClaimType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClaimRepository extends JpaRepository<Claim, Long> {

    Optional<Claim> findByTrackingId(String trackingId);

    long countByUanMaster_UanAndClaimType(String uan, ClaimType claimType);

    List<Claim> findByClaimStatus(ClaimStatus claimStatus);

    List<Claim> findByUanMaster_Uan(String uan);
}