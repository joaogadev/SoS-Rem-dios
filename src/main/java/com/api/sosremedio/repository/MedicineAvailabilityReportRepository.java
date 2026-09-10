package com.api.sosremedio.repository;

import com.api.sosremedio.model.MedicineAvailabilityReportModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MedicineAvailabilityReportRepository extends JpaRepository<MedicineAvailabilityReportModel, UUID> {
    List<MedicineAvailabilityReportModel> findByPharmacyMedicine_Id(UUID pharmacyId); //ordem decrescente

    List<MedicineAvailabilityReportRepository> findByUser_id(UUID userId); //ordem decrescnende por data de criação
}
