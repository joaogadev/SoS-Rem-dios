package com.api.sosremedio.repository;

import com.api.sosremedio.model.PharmacyMedicineModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PharmacyMedicineRepository extends JpaRepository<PharmacyMedicineModel, UUID> {
    boolean existsByPharmacyIdAndMedicineId(UUID pharmacyId, UUID medicineId);

    Optional<PharmacyMedicineModel> findByPharmacyIdAndMedicineId(UUID pharmacyId, UUID medicineId);

}
