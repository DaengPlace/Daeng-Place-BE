package com.mycom.backenddaengplace.pet.service;

import com.mycom.backenddaengplace.pet.domain.BreedType;
import com.mycom.backenddaengplace.pet.dto.request.BreedSearchRequest;
import com.mycom.backenddaengplace.pet.dto.response.BreedSearchResponse;
import com.mycom.backenddaengplace.pet.dto.response.BreedTypeResponse;
import com.mycom.backenddaengplace.pet.exception.BreedNotFoundException;
import com.mycom.backenddaengplace.pet.repository.BreedTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BreedService {
    private final BreedTypeRepository breedTypeRepository;

    public BreedSearchResponse searchBreedTypes(BreedSearchRequest request) {
        List<BreedType> breeds;

        if (request.getKeyword() == null || request.getKeyword().trim().isEmpty()) {
            breeds = breedTypeRepository.findAll();
        } else {
            breeds = breedTypeRepository.findByBreedTypeContaining(request.getKeyword().trim());
        }

        List<BreedTypeResponse> responses = breeds.stream()
                .map(breed -> BreedTypeResponse.builder()
                        .breedTypeId(breed.getId())
                        .breedType(breed.getBreedType())
                        .build())
                .collect(Collectors.toList());

        return BreedSearchResponse.builder()
                .totalCount(responses.size())
                .breeds(responses)
                .build();
    }

    @Transactional
    public BreedType getBreedType(Long breedTypeId) {
        return breedTypeRepository.findById(breedTypeId)
                .orElseThrow(() -> new BreedNotFoundException(breedTypeId));
    }

    public Optional<BreedType> findBreedByName(String breedName) {
        return breedTypeRepository.findByName(breedName);
    }
}
