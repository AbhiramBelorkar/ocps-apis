package com.ocps.auth.repository;

import com.ocps.auth.entity.Claim;
import com.ocps.auth.entity.ClaimStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClaimStatusHistoryRepository extends JpaRepository<ClaimStatusHistory, Long> {

    List<ClaimStatusHistory> findByClaimOrderByActionTimeAsc(Claim claim);
}