package com.ocps.claim.repository;

import com.ocps.claim.entity.Claim;
import com.ocps.claim.entity.ClaimStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClaimStatusHistoryRepository extends JpaRepository<ClaimStatusHistory, Long> {

    List<ClaimStatusHistory> findByClaimOrderByActionTimeAsc(Claim claim);
}