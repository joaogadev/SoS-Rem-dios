package com.api.sosremedio.repository;

import com.api.sosremedio.model.MedicinePriceHistoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MedicinePriceHistoryRepository extends JpaRepository<MedicinePriceHistoryModel,UUID> {
    List<MedicinePriceHistoryModel> findByMedicine_Id(UUID medicineId); //oredem crescente
}
