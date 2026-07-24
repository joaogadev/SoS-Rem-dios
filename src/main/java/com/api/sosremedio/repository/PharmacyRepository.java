package com.api.sosremedio.repository;

import com.api.sosremedio.model.PharmacyModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PharmacyRepository extends JpaRepository<PharmacyModel, UUID> {
    boolean existsByIdAndOwnerId(UUID pharmacyId, UUID ownerId);

}
