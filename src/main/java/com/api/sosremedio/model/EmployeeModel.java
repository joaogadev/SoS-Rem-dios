package com.api.sosremedio.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "employees",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_employee_user_pharmacy",
                        columnNames = {"user_id", "pharmacy_id"}
                )
        }
)
@Getter
@NoArgsConstructor
public class EmployeeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "pharmacy_id",  nullable = false)
    private PharmacyModel pharmacy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",  nullable = false)
    private UserModel user;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "position",  nullable = false, updatable = false)
    private String position;

    public EmployeeModel(PharmacyModel pharmacy,
                         UserModel user,
                          String position) {
        this.pharmacy = pharmacy;
        this.user = user;
        this.position = position;
    }
}
