package com.mycom.backenddaengplace.trait.service;

import com.mycom.backenddaengplace.pet.domain.Pet;
import com.mycom.backenddaengplace.pet.exception.PetNotFoundException;
import com.mycom.backenddaengplace.pet.repository.PetRepository;
import com.mycom.backenddaengplace.trait.domain.*;
import com.mycom.backenddaengplace.trait.dto.request.PetTraitResponseRequestList;
import com.mycom.backenddaengplace.trait.dto.request.TraitResponseRequest;
import com.mycom.backenddaengplace.trait.exception.TraitAnswerNotFoundException;
import com.mycom.backenddaengplace.trait.exception.TraitQuestionNotFoundException;
import com.mycom.backenddaengplace.trait.repository.PetTraitResponseRepository;
import com.mycom.backenddaengplace.trait.repository.TraitAnswerRepository;
import com.mycom.backenddaengplace.trait.repository.TraitQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetTraitService {

    private final TraitAnswerRepository traitAnswerRepository;
    private final TraitQuestionRepository traitQuestionRepository;
    private final PetTraitResponseRepository petTraitResponseRepository;
    private final PetRepository petRepository;

    @Transactional
    public void savePetTraitResponse(PetTraitResponseRequestList requestList, Long petId) {
        petTraitResponseRepository.deleteByPetId(petId);

        for (TraitResponseRequest request : requestList.getPetTraitResponseRequestList()) {

            Pet pet = petRepository.findById(petId)
                    .orElseThrow(() -> new PetNotFoundException(petId));

            TraitQuestion traitQuestion = traitQuestionRepository.findById(request.getTraitQuestionId())
                    .orElseThrow(() -> new TraitQuestionNotFoundException(request.getTraitQuestionId()));

            TraitAnswer traitAnswer = traitAnswerRepository.findById(request.getTraitAnswerId())
                    .orElseThrow(() -> new TraitAnswerNotFoundException(request.getTraitAnswerId()));

            // 복합키 생성
            PetTraitResponseId responseId = new PetTraitResponseId(
                    petId,
                    request.getTraitAnswerId(),
                    request.getTraitQuestionId()
            );

            PetTraitResponse response = PetTraitResponse.builder()
                    .id(responseId)
                    .pet(pet)
                    .traitQuestion(traitQuestion)
                    .traitAnswer(traitAnswer)
                    .build();

            petTraitResponseRepository.save(response);
        }
    }

}
