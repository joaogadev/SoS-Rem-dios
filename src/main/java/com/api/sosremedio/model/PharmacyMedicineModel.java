package com.api.sosremedio.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "pharmacy_medicines",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_pharmacy_medicine",
                        columnNames = {"pharmacy_id", "medicine_id"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PharmacyMedicineModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacy_id", nullable = false)
    private PharmacyModel pharmacy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", nullable = false)
    private MedicineModel medicine;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal currentPrice;

    @Column(name = "stock")
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(/*name = "availability_status"*/, nullable = false, length = 30)
    private AvailabilityStatus status = AvailabilityStatus.UNKNOWN;

    @Column(/*name = "last_confirmed_at"*/)
    private LocalDateTime lastConfirmedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(/*name = "last_confirmed_by"*/)
    private UserModel lastConfirmedBy;

    @Enumerated(EnumType.STRING)
    @Column(/*name = "confirmation_source", */length = 30)
    private ConfirmationSource source;

    @CreationTimestamp
    @Column(/*name = "created_at", */nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(/*name = "updated_at", */nullable = false)
    private LocalDateTime updatedAt;

    public PharmacyMedicineModel(
            PharmacyModel pharmacy,
            MedicineModel medicine,
            BigDecimal price,
            int stock,
            AvailabilityStatus status,
            UserModel lastConfirmedBy,
            ConfirmationSource source
    ) {

        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }

        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price cannot be zero or negative");
        }

        this.pharmacy = pharmacy;
        this.medicine = medicine;
        this.currentPrice = price;
        this.stock = stock;
        this.status = status;
        this.lastConfirmedBy = lastConfirmedBy;
        this.source = source;
    }

    public void updateStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }

        this.stock = stock;
    }

    public void updatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price cannot be zero or negative");
        }
        this.currentPrice = price;
    }

    public boolean hasAvailableStock(int quantity) {
        return this.stock >= quantity;
    }

    public void decreaseStock(int quantity) {
        if (quantity <= 0 ) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }

        if (!hasAvailableStock(quantity)) {
            throw new IllegalStateException("Stock is out of stock");
        }

        this.stock -= quantity;
    }
}
