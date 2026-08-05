package com.api.sosremedio.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "medicines")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MedicineModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "active_ingredient", nullable = false, length = 255)
    private String activeIngredient;

    @Column(name = "dosage", nullable = false, length = 255)
    private String dosage;

    @Column(name = "pharmaceutical_form", nullable = false, length = 255)
    private String pharmaceuticalForm;

    @Column(name = "manufacturer", nullable = false, length = 255)
    private String manufacturer;

    @Column(name = "requires_prescription", nullable = false, length = 255)
    private boolean requiresPrescription;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public MedicineModel(String name, String description, String activeIngredient, String dosage, String pharmaceuticalForm, String manufacturer, boolean requiresPrescription) {
        this.name = name;
        this.description = description;
        this.activeIngredient = activeIngredient;
        this.dosage = dosage;
        this.pharmaceuticalForm = pharmaceuticalForm;
        this.manufacturer = manufacturer;
        this.requiresPrescription = requiresPrescription;
    }
}
