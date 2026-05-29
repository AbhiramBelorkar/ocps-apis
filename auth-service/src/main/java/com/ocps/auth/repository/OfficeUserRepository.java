package com.ocps.auth.repository;

import com.ocps.auth.entity.OfficeUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfficeUserRepository extends JpaRepository<OfficeUser, Long> {

    Optional<OfficeUser> findByUserId(Long userId);
}
