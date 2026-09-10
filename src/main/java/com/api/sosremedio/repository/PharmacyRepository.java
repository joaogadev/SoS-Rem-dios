package com.api.sosremedio.repository;

import com.api.sosremedio.model.PharmacyModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PharmacyRepository extends JpaRepository<PharmacyModel, UUID> {
    //entre em owner e compare id
    boolean existsByIdAndOwner_id(UUID pharmacyId, UUID ownerId);

    boolean existsByCnpj(String cnpj);
    boolean existsByEmail(String email);

    List<PharmacyModel> findByOwner_id(UUID ownerId);
}
