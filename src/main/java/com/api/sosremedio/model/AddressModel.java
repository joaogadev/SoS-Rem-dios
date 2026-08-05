package com.api.sosremedio.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "address")
@Getter
@NoArgsConstructor
public class AddressModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "zip_code", nullable = false, length = 8)
    private String zipcode;

    @Column(name = "state", length = 2)
    private String state;

    @Column(name = "city", length = 255)
    private String city;

    @Column(name = "neighborhood", length = 255)
    private String neighborhood;

    @Column(name = "street", length = 255)
    private String street;

    @Column(name = "number", length = 10)
    private String number;

    @Column(name = "complement", length = 255)
    private String complement;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, updatable = false)
    private LocalDateTime updatedAt;

    public AddressModel(String zipcode,
                        String state,
                        String city,
                        String neighborhood,
                        String street,
                        String number,
                        String complement
    ) {
        this.zipcode = zipcode;
        this.state = state;
        this.city = city;
        this.neighborhood = neighborhood;
        this.street = street;
        this.number = number;
        this.complement = complement;
    }
}
