package com.api.sosremedio.services;

import com.api.sosremedio.repository.EmployeeRepository;
import com.api.sosremedio.repository.PharmacyRepository;
import com.api.sosremedio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
//validata o vinvulo do usuario, se ele é dono ou funcionario da farmacia, para poder acessar os dados da mesma
public class PharmacyPermissionService {
    private final UserRepository  userRepository;
    private final PharmacyRepository pharmacyRepository;
    private final EmployeeRepository employeeRepository;
    private final CurrentUserService currentUserService;

    public void validateOwnerOrEmployeer (UUID pharmacyID) {
        UUID currentUserId = currentUserService.getCurrentUserId();

        boolean isOwner = pharmacyRepository.existsByIdAndOwnerId(pharmacyID, currentUserId);

        boolean isEmployee = employeeRepository.existsPharmacyByIdAndUserId(pharmacyID, currentUserId);

        if (!isOwner && !isEmployee) {
            throw new AccessDeniedException("You havent access to this pharmacy");
        }
    }

    public void validateOwner (UUID pharmacyId) {
        UUID currentUserId = currentUserService.getCurrentUserId();

        boolean isOwner = pharmacyRepository.existsByIdAndOwnerId(currentUserId, pharmacyId);

        if (!isOwner) {
            throw new AccessDeniedException("You havent access to this pharmacy");
        }
    }
}
