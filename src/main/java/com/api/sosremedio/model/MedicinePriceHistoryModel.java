package com.api.sosremedio.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "medicine_price_history")
@Getter
@NoArgsConstructor
//guarda cada preço informado ao longo do tempo
public class MedicinePriceHistoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacy_medicine_id", nullable = false)
    private PharmacyMedicineModel pharmacyMedicine;

    @Column(name = "price", length = 30)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false, length = 30)
    private ConfirmationSource source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_by", nullable = false)
    private UserModel reportedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public MedicinePriceHistoryModel(PharmacyMedicineModel pharmacyMedicine, BigDecimal price, ConfirmationSource source, UserModel reportedBy) {
        this.pharmacyMedicine = pharmacyMedicine;
        this.price = price;
        this.source = source;
        this.reportedBy = reportedBy;
    }
}
