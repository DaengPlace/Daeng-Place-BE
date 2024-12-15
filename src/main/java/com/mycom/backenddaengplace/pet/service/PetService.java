package com.mycom.backenddaengplace.pet.service;

import com.mycom.backenddaengplace.pet.domain.BreedType;
import com.mycom.backenddaengplace.pet.domain.Pet;
import com.mycom.backenddaengplace.pet.dto.request.BasePetRequest;
import com.mycom.backenddaengplace.pet.dto.response.BasePetResponse;
import com.mycom.backenddaengplace.pet.dto.response.PetDeleteResponse;
import com.mycom.backenddaengplace.pet.exception.InvalidBirthDateException;
import com.mycom.backenddaengplace.pet.exception.PetNotFoundException;
import com.mycom.backenddaengplace.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PetService {
    private final PetRepository petRepository;
    private final BreedService breedService;  // BreedService 주입

    public BasePetResponse registerPet(BasePetRequest request) {
        log.debug("반려견 등록 시작");

        // breed 문자열을 통해 BreedType 찾기
        BreedType breedType = breedService.findBreedByName(request.getBreed())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 견종입니다: " + request.getBreed())); // BreedService 사용

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


    public BasePetResponse revisePet(BasePetRequest request, Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException(petId));

        // breed 문자열을 통해 BreedType 찾기
        BreedType breedType = breedService.findBreedByName(request.getBreed())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 견종입니다: " + request.getBreed()));
        LocalDateTime birthDate = parseBirthDate(request.getBirthDate());

        pet.setBreedType(breedType);
        pet.setName(request.getName());
        pet.setBirthDate(birthDate);
        pet.setIsNeutered(request.getIsNeutered());
        pet.setGender(request.getGender());
        pet.setWeight(request.getWeight());

        petRepository.save(pet);

        return BasePetResponse.from(pet);
    }

    public BasePetResponse getPet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException(petId));

        return BasePetResponse.from(pet);
    }

    public List<BasePetResponse> getPets(Long memberId) {
        return petRepository.findByMemberId(memberId).stream().map(BasePetResponse::from).toList();
    }

    public PetDeleteResponse deletePet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException(petId));
        petRepository.deleteById(pet.getId());
        return new PetDeleteResponse(pet.getId(), LocalDateTime.now());
    }

    private LocalDateTime parseBirthDate(String birthDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            return LocalDate.parse(birthDate, formatter).atStartOfDay();
        } catch (DateTimeParseException e) {
            throw new InvalidBirthDateException();
        }
    }
}
