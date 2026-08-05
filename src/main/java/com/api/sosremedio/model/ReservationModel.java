package com.api.sosremedio.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",  nullable = false)
    private UserModel customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacy_medicine_id",  nullable = false)
    private PharmacyMedicineModel pharmacyMedicine;

    @Column(name = "quantity")
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ReservationStatus status;

    @Column(name = "reservation_code", nullable = false, unique = true, length = 10)
    private String reservationCode;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public ReservationModel(UserModel customer,
                            PharmacyMedicineModel pharmacyMedicine,
                            int quantity, ReservationStatus status,
                            String reservationCode,
                            LocalDateTime expiresAt
    ) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        this.customer = customer;
        this.pharmacyMedicine = pharmacyMedicine;
        this.quantity = quantity;
        this.status = status;
        this.reservationCode = reservationCode;
        this.expiresAt = expiresAt;
    }

    public void confirm() {
        if (this.status != ReservationStatus.PENDING) {
            throw new IllegalArgumentException("Status must be PENDING.");
        }

        this.status = ReservationStatus.CONFIRMED;
    }

    public void cancel(){
        if (this.status == ReservationStatus.EXPIRED || this.status == ReservationStatus.COMPLETED) {
            throw new IllegalArgumentException("Cannot cancel an expired or completed reservation.");
        }

        if (this.status == ReservationStatus.CANCELLED) {
            throw new IllegalArgumentException("Reservation already cancelled.");
        }
        this.status = ReservationStatus.CANCELLED;
    }

    public void complete() {
        if (this.status != ReservationStatus.CONFIRMED) {
            throw new IllegalArgumentException("Status must be CONFIRMED.");
        }

        this.status = ReservationStatus.COMPLETED;
    }

    public void expire() {
        if (this.status != ReservationStatus.PENDING) {
            throw new IllegalArgumentException("Status must be PENDING.");
        }
        this.status = ReservationStatus.EXPIRED;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }
}
