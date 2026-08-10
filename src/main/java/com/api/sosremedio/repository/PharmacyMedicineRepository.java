package com.api.sosremedio.repository;

import com.api.sosremedio.model.PharmacyMedicineModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PharmacyMedicineRepository extends JpaRepository<PharmacyMedicineModel, UUID> {
    boolean existsByPharmacy_IdAndMedicine_Id(UUID pharmacyId, UUID medicineId);

    Optional<PharmacyMedicineModel> findByPharmacy_IdAndMedicine_Id(UUID pharmacyId, UUID medicineId);

    List<PharmacyMedicineModel> findByPharmacy_Id(UUID pharmacyId);

    List<PharmacyMedicineModel> findByMedicine_Id(UUID medicineId);

    List<PharmacyMedicineModel> findBymedicine_Name(String medicineName); // com ignorecase

}
