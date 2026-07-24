package com.api.sosremedio.repository;

import com.api.sosremedio.model.AdressModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdressRepository extends JpaRepository<AdressModel, UUID> {
}
