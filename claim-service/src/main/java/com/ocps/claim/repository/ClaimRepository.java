package com.ocps.claim.repository;

import com.ocps.claim.entity.Claim;
import com.ocps.claim.enums.ClaimStatus;
import com.ocps.claim.enums.ClaimType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {

    Optional<Claim> findByTrackingId(String trackingId);

    long countByUanAndClaimType(String uan, ClaimType claimType);

    List<Claim> findByClaimStatus(ClaimStatus claimStatus);

    List<Claim> findByUan(String uan);
}