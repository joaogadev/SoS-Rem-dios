package com.api.sosremedio.repository;

import com.api.sosremedio.model.EmployeeModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<EmployeeModel, UUID> {
    boolean existsPharmacyBy_IdAndUser_Id(UUID pharmacyId, UUID userId);
}
