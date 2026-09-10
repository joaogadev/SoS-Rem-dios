package com.api.sosremedio.repository;

import com.api.sosremedio.model.MedicineModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MedicineRepository extends JpaRepository<MedicineModel, UUID> {
    List<MedicineModel> findByNameContaining(String name);

    boolean existsByNameAndDosageAndManufacturer(
            String name,
            String dosage,
            String manufacturer
    ); //com ignoreCase
}
