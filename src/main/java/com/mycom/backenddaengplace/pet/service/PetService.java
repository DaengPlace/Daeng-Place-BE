package com.mycom.backenddaengplace.pet.service;

import com.mycom.backenddaengplace.pet.domain.BreedType;
import com.mycom.backenddaengplace.pet.domain.Pet;
import com.mycom.backenddaengplace.pet.dto.request.BasePetRequest;
import com.mycom.backenddaengplace.pet.dto.response.*;
import com.mycom.backenddaengplace.pet.exception.BreedNotFoundException;
import com.mycom.backenddaengplace.pet.exception.InvalidBirthDateException;
import com.mycom.backenddaengplace.pet.exception.PetNotFoundException;
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
    private final PetApiService petApiService;

    public BasePetResponse registerPet(BasePetRequest request) {
        log.debug("반려견 등록 시작");

        BreedType breedType = getBreedType(request.getBreedTypeId());

        LocalDateTime birthDate = parseBirthDate(request.getBirthDate());

        Pet pet = Pet.builder()
                .breedType(breedType)
                .name(request.getName())
                .birthDate(birthDate)
                .isNeutered(request.getIsNeutered())
                .gender(request.getGender())
                .weight(request.getWeight())
                .build();

        Pet savedPet = petRepository.save(pet);
        log.info("반려견 등록 완료. petId: {}", savedPet.getId());

        return BasePetResponse.from(pet);
    }

    private BreedType getBreedType(Long breedTypeId) {
        String breedName = petApiService.getBreedById(breedTypeId);
        BreedType breedType = BreedType.builder()
                .id(breedTypeId)
                .breedType(breedName)
                .build();
        return breedTypeRepository.save(breedType);
    }

    public List<BreedTypeResponse> getAllBreedTypes() {
        return petApiService.getAllBreedTypes().stream()
                .map(breedType -> BreedTypeResponse.builder()
                        .breedTypeId(breedType.getBreedTypeId())
                        .breedType(breedType.getBreedType())
                        .build())
                .collect(Collectors.toList());
    }

    public BasePetResponse getPet(Long petId) {

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException(petId));

        return BasePetResponse.from(pet);
    }

    public BasePetResponse revisePet(BasePetRequest request, Long petId) {

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException(petId));

        // BreedType 조회
        BreedType breedType = getBreedType(request.getBreedTypeId());

        // BirthDate 변환
        LocalDateTime birthDate = parseBirthDate(request.getBirthDate());

        // Pet 객체 수정
        pet.setBreedType(breedType);
        pet.setName(request.getName());
        pet.setBirthDate(birthDate);
        pet.setIsNeutered(request.getIsNeutered());
        pet.setGender(request.getGender());
        pet.setWeight(request.getWeight());

        // 수정된 Pet 객체 저장
        petRepository.save(pet);

        return BasePetResponse.from(pet);
    }

    public PetDeleteResponse deletePet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException(petId));
        petRepository.deleteById(pet.getId());
        return new PetDeleteResponse(pet.getId(), LocalDateTime.now());
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
