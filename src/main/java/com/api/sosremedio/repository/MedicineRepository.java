package com.api.sosremedio.repository;

import com.api.sosremedio.model.MedicineModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MedicineRepository extends JpaRepository<MedicineModel, UUID> {
}
