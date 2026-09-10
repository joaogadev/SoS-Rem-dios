package com.api.sosremedio.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "medicine_availability_reports")
@NoArgsConstructor
@Getter
//guarda relatos de disponibilidade feitos por usuários, farmácias ou admin
public class MedicineAvailabilityReportModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacy_medicine_id")
    private PharmacyMedicineModel pharmacyMedicine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserModel user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private AvailabilityStatus status;

    @Column(name = "reported_price", precision = 10, scale = 2)
    private BigDecimal reportedPrice;

    @Column(name = "notes", length = 500)
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false, length = 30)
    private ConfirmationSource source;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public MedicineAvailabilityReportModel(PharmacyMedicineModel pharmacyMedicine, UserModel user, AvailabilityStatus status, BigDecimal reportedPrice, String notes, ConfirmationSource source) {
        this.pharmacyMedicine = pharmacyMedicine;
        this.user = user;
        this.status = status;
        this.reportedPrice = reportedPrice;
        this.notes = notes;
        this.source = source;
    }
}
