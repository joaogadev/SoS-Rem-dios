package com.api.sosremedio.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "pharmacies")
@NoArgsConstructor
@Getter
public class PharmacyModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id",  nullable = false)
    private UserModel owner;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "cnpj", nullable = false, length = 14)
    private String cnpj;

    @Column(name = "phone" ,length = 20)
    private String phone;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @JoinColumn(name = "address_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private AddressModel address;

    @Column(name = "opening_hours")
    private LocalTime openingHours;

    @Column(name = "closing_hours")
    private LocalTime closingHours;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public PharmacyModel(
            UserModel owner,
            String name,
            String cnpj,
            String phone,
            String email,
            AddressModel address,
            LocalTime openingHours,
            LocalTime closingHours
    ) {
        this.owner = owner;
        this.name = name;
        this.cnpj = cnpj;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.openingHours = openingHours;
        this.closingHours = closingHours;
    }
}