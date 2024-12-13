package com.mycom.backenddaengplace.trait.service;

import com.mycom.backenddaengplace.place.dto.response.PopularPlaceResponse;
import com.mycom.backenddaengplace.place.enums.Category;
import com.mycom.backenddaengplace.place.repository.PlaceQueryRepository;
import com.mycom.backenddaengplace.trait.domain.PetTraitResponse;
import com.mycom.backenddaengplace.trait.repository.PetTraitResponseRepository;
import com.mycom.backenddaengplace.trait.repository.PlaceRecommendationQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecommendationService {
    private static final int RECOMMENDATION_SIZE = 3;

    private final PetTraitResponseRepository petTraitResponseRepository;
    private final PlaceQueryRepository placeQueryRepository;
    private final PlaceRecommendationQueryRepository recommendationQueryRepository;

    public List<PopularPlaceResponse> recommendPlaces(Long petId) {
        // 1. 반려견의 성향 진단 결과 확인
        List<PetTraitResponse> petTraits = petTraitResponseRepository.findByPetId(petId);

        // 성향 진단이 없는 경우 -> 인기 시설 반환
        if (petTraits.isEmpty()) {
            return placeQueryRepository.findPopularPlaces("popularity", null, PageRequest.of(0, RECOMMENDATION_SIZE))
                    .getContent();
        }

        // 2. 성향에 기반한 카테고리 결정
        Set<Category> recommendedCategories = determineRecommendedCategories(petTraits);

        // 3. 카테고리에 해당하는 장소들 중 평점 높은 순으로 조회
        return recommendationQueryRepository.findRecommendedPlaces(recommendedCategories);
    }

    private Set<Category> determineRecommendedCategories(List<PetTraitResponse> petTraits) {
        Set<Category> categories = new HashSet<>();

        for (PetTraitResponse trait : petTraits) {
            Long questionId = trait.getTraitQuestion().getId();
            Long answerId = trait.getTraitAnswer().getId();

            // 활동성 (questionId: 4)
            if (questionId == 4L) {
                if (answerId == 7L) { // 활발함
                    categories.addAll(Arrays.asList(Category.여행지, Category.카페));
                } else { // 점잖음
                    categories.addAll(Arrays.asList(Category.반려동물용품, Category.카페));
                }
            }
            // 타견 사교성 (questionId: 5)
            else if (questionId == 5L) {
                if (answerId == 9L) { // 잘 어울림
                    categories.addAll(Arrays.asList(Category.카페, Category.식당));
                } else { // 낯을 가림
                    categories.addAll(Arrays.asList(Category.미용, Category.동물병원));
                }
            }
            // 대인관계 (questionId: 6)
            else if (questionId == 6L) {
                if (answerId == 11L) { // 잘 다가감
                    categories.addAll(Arrays.asList(Category.카페, Category.식당, Category.여행지));
                } else { // 경계
                    categories.addAll(Arrays.asList(Category.미용, Category.동물병원, Category.반려동물용품));
                }
            }
        }

        return categories;
    }
}