package com.api.sosremedio.services;

import com.api.sosremedio.dto.request.CreatePharmacyRequest;
import com.api.sosremedio.dto.response.PharmacyResponse;
import com.api.sosremedio.model.AddressModel;
import com.api.sosremedio.model.PharmacyModel;
import com.api.sosremedio.model.UserModel;
import com.api.sosremedio.model.UserRole;
import com.api.sosremedio.repository.PharmacyRepository;
import com.api.sosremedio.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PharmacyService {

    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private final PharmacyRepository pharmacyRepository;
    private final PharmacyPermissionService pharmacyPermissionService;

    @Transactional
    public PharmacyResponse create(CreatePharmacyRequest request) {
        String normalizedEmail = normalizeEmail(request.email());
        String normalizedPhone = normalizePhone(request.phone());
        
        validateOpeningHours(request.openingHours(), request.closingHours());

        if (pharmacyRepository.existsByCnpj(normalizeCnpj(request.cnpj()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Pharmacy already exists with this cnpj");
        }

        if (pharmacyRepository.existsByEmail(normalizedEmail)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Pharmacy already exists with this email");
        }

        UUID ownerId = currentUserService.getCurrentUserId();
        UserModel owner = userRepository.findById(ownerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found"));

        if (owner.getRole() != UserRole.PHARMACY_OWNER) {
            throw new AccessDeniedException("User does not have permission to create a pharmacy");
        }

        AddressModel address = new AddressModel(
                request.address().zipcode(),
                request.address().state(),
                request.address().city(),
                request.address().neighborhood(),
                request.address().street(),
                request.address().number(),
                request.address().complement()
        );

        PharmacyModel pharmacy = new PharmacyModel(
                owner,
                request.name().trim(),
                normalizeCnpj(request.cnpj()),
                normalizedPhone,
                normalizedEmail,
                address,
                request.openingHours(),
                request.closingHours()
        );

        PharmacyModel savedPharmacy = pharmacyRepository.save(pharmacy);

        return PharmacyResponse.from(savedPharmacy);
    }

    @Transactional
    public PharmacyResponse update(UUID pharmacyId, CreatePharmacyRequest request) {

        PharmacyModel pharmacy = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pharmacy not found"));

        pharmacyPermissionService.validateOwner(pharmacyId);

        //if (!pharmacy.getOwner().getId().equals(currentUserId)) {
        //    throw new AccessDeniedException("User does not have permission to update this pharmacy");
        //}

        String normalizedCnpj = normalizeCnpj(request.cnpj());
        String normalizedEmail = normalizeEmail(request.email());
        String normalizedPhone = normalizePhone(request.phone());

        validateOpeningHours(request.openingHours(), request.closingHours());

        if (!pharmacy.getCnpj().equals(normalizeCnpj(normalizedCnpj)) && pharmacyRepository.existsByCnpj(normalizeCnpj(normalizedCnpj))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Pharmacy already exists with this cnpj");
        }

        if (!pharmacy.getEmail().equals(normalizedEmail) && pharmacyRepository.existsByEmail(normalizedEmail)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Pharmacy already exists with this email");
        }

        pharmacy.updateData(
                request.name().trim(),
                normalizedCnpj,
                normalizedPhone,
                normalizedEmail,
                request.openingHours(),
                request.closingHours()
        );

        return PharmacyResponse.from(pharmacy);
    }

    public void delete(UUID pharmacyId) {
        PharmacyModel pharmacy = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pharmacy not found"));

        pharmacyPermissionService.validateOwner(pharmacyId);

        pharmacyRepository.delete(pharmacy);
    }

    public List<PharmacyResponse> getAll() {
        pharmacyPermissionService.validateOwner(currentUserService.getCurrentUserId());

        List<PharmacyModel> pharmacies = pharmacyRepository.findAll();
        return pharmacies.stream().map(PharmacyResponse::from).toList();
    }

    public PharmacyResponse findById(UUID pharmacyId) {
        pharmacyPermissionService.validateOwner(pharmacyId);

        return pharmacyRepository.findById(pharmacyId)
                .map(PharmacyResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pharmacy not found"));
    }

    private String normalizePhone(String phone) {
        if (phone == null || phone.isBlank()) return null;
        return phone.trim();
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizeCnpj(String cnpj) {
        if (cnpj == null || cnpj.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cnpj is required");
        }

        String onlyDigits = cnpj.trim().replaceAll("\\D", "");

        if (onlyDigits.length() != 14) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cnpj length must be 14 digits");
        }

        return onlyDigits;
    }

    private void validateOpeningHours(LocalTime open, LocalTime close) {
        if (!close.isAfter(open)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Closing hours must be after opening hours");
        }
    }
}
