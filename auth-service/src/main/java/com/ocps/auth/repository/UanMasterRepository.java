package com.ocps.auth.repository;

import com.ocps.auth.entity.UanMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UanMasterRepository extends JpaRepository<UanMaster, Long> {

    Optional<UanMaster> findByUan(String uan);

}