package com.mycom.backenddaengplace.pet.service;

import com.mycom.backenddaengplace.common.service.S3ImageService;
import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.member.exception.MemberNotFoundException;
import com.mycom.backenddaengplace.member.repository.MemberRepository;
import com.mycom.backenddaengplace.pet.domain.BreedType;
import com.mycom.backenddaengplace.pet.domain.Pet;
import com.mycom.backenddaengplace.pet.dto.request.BasePetRequest;
import com.mycom.backenddaengplace.pet.dto.response.BasePetResponse;
import com.mycom.backenddaengplace.pet.dto.response.PetDeleteResponse;
import com.mycom.backenddaengplace.pet.exception.InvalidBirthDateException;
import com.mycom.backenddaengplace.pet.exception.PetNotFoundException;
import com.mycom.backenddaengplace.pet.exception.PetNotOwnedException;
import com.mycom.backenddaengplace.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final BreedService breedService;
    private final MemberRepository memberRepository;
    private final S3ImageService s3ImageService;

    @Transactional
    public BasePetResponse registerPet(BasePetRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        BreedType breedType = breedService.findBreedByName(request.getBreed())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 견종입니다: " + request.getBreed()));

        Pet pet = Pet.builder()
                .member(member)  // member 설정 추가
                .breedType(breedType)
                .name(request.getName())
                .birthDate(parseBirthDate(request.getBirthDate()))
                .isNeutered(request.getIsNeutered())
                .gender(request.getGender())
                .weight(request.getWeight())
                .build();

        return BasePetResponse.from(petRepository.save(pet));
    }


    @Transactional
    public BasePetResponse revisePet(BasePetRequest request, Long petId, Long memberId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException(petId));

        // 해당 펫의 소유자인지 확인
        if (!pet.getMember().getId().equals(memberId)) {
            throw new PetNotOwnedException(memberId, petId);
        }

        BreedType breedType = breedService.findBreedByName(request.getBreed())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 견종입니다: " + request.getBreed()));

        pet.setBreedType(breedType);
        pet.setName(request.getName());
        pet.setBirthDate(parseBirthDate(request.getBirthDate()));
        pet.setIsNeutered(request.getIsNeutered());
        pet.setGender(request.getGender());
        pet.setWeight(request.getWeight());

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

    @Transactional
    public PetDeleteResponse deletePet(Long petId, Long memberId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException(petId));

        // 해당 펫의 소유자인지 확인
        if (!pet.getMember().getId().equals(memberId)) {
            throw new PetNotOwnedException(memberId, petId);
        }

        petRepository.delete(pet);
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

    @Transactional
    public BasePetResponse updateProfileImage(Long petId, MultipartFile file, Long memberId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException(petId));

        // 해당 펫의 소유자인지 확인
        if (!pet.getMember().getId().equals(memberId)) {
            throw new PetNotOwnedException(memberId, petId);
        }

        // 기존 이미지가 있다면 삭제
        if (pet.getProfileImageUrl() != null) {
            s3ImageService.deleteImage(pet.getProfileImageUrl());
        }

        // 새 이미지 업로드 및 URL 저장
        String imageUrl = s3ImageService.uploadImage(file, S3ImageService.PET_PROFILE_DIR);
        pet.setProfileImageUrl(imageUrl);

        return BasePetResponse.from(pet);
    }
}
