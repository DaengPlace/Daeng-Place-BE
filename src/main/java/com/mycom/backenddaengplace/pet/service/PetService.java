package com.mycom.backenddaengplace.pet.service;

import com.mycom.backenddaengplace.pet.domain.BreedType;
import com.mycom.backenddaengplace.pet.domain.Pet;
import com.mycom.backenddaengplace.pet.dto.request.PetRegisterRequest;
import com.mycom.backenddaengplace.pet.dto.response.BreedTypeResponse;
import com.mycom.backenddaengplace.pet.dto.response.PetRegisterResponse;
import com.mycom.backenddaengplace.pet.exception.BreedNotFoundException;
import com.mycom.backenddaengplace.pet.exception.InvalidBirthDateException;
import com.mycom.backenddaengplace.pet.repository.BreedTypeRepository;
import com.mycom.backenddaengplace.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PetService {
    private final PetRepository petRepository;
    private final BreedTypeRepository breedTypeRepository;

    public PetRegisterResponse registerPet(PetRegisterRequest request) {
        log.debug("반려견 등록 시작");

        BreedType breedType = breedTypeRepository.findById(request.getBreedTypeId())
                .orElseThrow(() -> new BreedNotFoundException(request.getBreedTypeId()));

        LocalDateTime birthDate = parseBirthDate(request.getBirthDate());

        Pet pet = Pet.builder()
                .breedType(breedType)
                .name(request.getName())
                .birthDate(birthDate)
                .isNeutered(request.getIsNeutered())
                .gender(request.getGender())
                .weight(request.getWeight().intValue())
                .build();

        Pet savedPet = petRepository.save(pet);
        log.info("반려견 등록 완료. petId: {}", savedPet.getId());

        return PetRegisterResponse.builder()
                .petId(savedPet.getId())
                .name(savedPet.getName())
                .breed(savedPet.getBreedType().getBreedType())
                .birthDate(savedPet.getBirthDate().format(DateTimeFormatter.ISO_DATE))
                .age(calculateAge(savedPet.getBirthDate()))
                .weight(savedPet.getWeight().doubleValue())
                .gender(savedPet.getGender())
                .isNeutered(savedPet.getIsNeutered())
                .registeredAt(savedPet.getCreatedAt())
                .build();
    }

    public List<BreedTypeResponse> getAllBreedTypes() {
        return breedTypeRepository.findAllSorted()
                .stream()
                .map(breedType -> BreedTypeResponse.builder()
                        .breedTypeId(breedType.getId())
                        .breedType(breedType.getBreedType())
                        .build())
                .collect(Collectors.toList());
    }

    private LocalDateTime parseBirthDate(String birthDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
            return LocalDate.parse(birthDate, formatter).atStartOfDay();
        } catch (DateTimeParseException e) {
            throw new InvalidBirthDateException();
        }
    }

    private String calculateAge(LocalDateTime birthDate) {
        LocalDateTime now = LocalDateTime.now();
        long months = ChronoUnit.MONTHS.between(birthDate, now);
        return months + "개월";
    }
}
