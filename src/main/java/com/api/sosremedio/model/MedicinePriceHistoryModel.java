package com.api.sosremedio.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
//guarda cada preço informado ao longo do tempo
public class MedicinePriceHistoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private PharmacyMedicineModel pharmacyMedicine;

    @Column(length = 30)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ConfirmationSource source;

    @ManyToOne
    private UserModel reportedBy;

    @CreationTimestamp
    @Column(/*name = "created_at",*/ nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public MedicinePriceHistoryModel(PharmacyMedicineModel pharmacyMedicine, BigDecimal price, ConfirmationSource source) {
        this.pharmacyMedicine = pharmacyMedicine;
        this.price = price;
        this.source = source;
    }
}
