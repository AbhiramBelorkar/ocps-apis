package com.ocps.claim.repository;

import com.ocps.claim.entity.Claim;
import com.ocps.claim.entity.ClaimStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimStatusHistoryRepository extends JpaRepository<ClaimStatusHistory, Long> {

    List<ClaimStatusHistory> findByClaimOrderByActionTimeAsc(Claim claim);
}