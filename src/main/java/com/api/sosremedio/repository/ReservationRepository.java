package com.api.sosremedio.repository;

import com.api.sosremedio.model.ReservationModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<ReservationModel, UUID> {
    List<ReservationModel> findByCustomerId(UUID customerId);
}
