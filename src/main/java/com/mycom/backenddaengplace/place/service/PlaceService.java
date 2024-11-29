package com.mycom.backenddaengplace.place.service;

import com.mycom.backenddaengplace.member.enums.Gender;
import com.mycom.backenddaengplace.member.exception.MemberNotFoundException;
import com.mycom.backenddaengplace.member.repository.MemberRepository;
import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.place.domain.Address;
import com.mycom.backenddaengplace.place.domain.OperationHour;
import com.mycom.backenddaengplace.place.domain.Place;
import com.mycom.backenddaengplace.place.dto.request.SearchCriteria;
import com.mycom.backenddaengplace.place.dto.response.PlaceDetailResponse;
import com.mycom.backenddaengplace.place.dto.response.PlaceListResponse;
import com.mycom.backenddaengplace.place.dto.response.PopularPlaceResponse;
import com.mycom.backenddaengplace.place.enums.Category;
import com.mycom.backenddaengplace.place.exception.PlaceNotFoundException;
import com.mycom.backenddaengplace.place.repository.OperationHourRepository;
import com.mycom.backenddaengplace.place.repository.PlaceQueryRepository;
import com.mycom.backenddaengplace.place.repository.PlaceRepository;
import com.mycom.backenddaengplace.review.domain.Review;
import com.mycom.backenddaengplace.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final OperationHourRepository operationHourRepository;
    private final ReviewRepository reviewRepository;
    private final PlaceQueryRepository placeQueryRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public PlaceDetailResponse getPlaceDetail(Long placeId) {

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceNotFoundException(placeId));

        // 오늘의 요일 계산
        String todayName = LocalDateTime.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);

        // 오늘 요일의 OperationHour 조회
        List<OperationHour> operationHours = operationHourRepository.findByPlaceId(placeId);
        String startTime = null;
        String endTime = null;
        List<String> holidays = new ArrayList<>();
        for (OperationHour hour : operationHours) {
            if (hour.getIsDayOff()) {
                holidays.add(hour.getDayOfWeek());
            } else if (hour.getDayOfWeek().equalsIgnoreCase(todayName)) {
                startTime = hour.getStartTime().toString();
                endTime = hour.getEndTime().toString();
            }
        }

        Address address = place.getAddress();
        String roadAddress = address != null ? address.getRoadAddress() : null;

        long reviewCount = reviewRepository.countByPlaceId(placeId);
        List<Review> latestReviews = reviewRepository.findTop3ByPlaceIdOrderByCreatedAtDesc(placeId);

        List<Map<String, Object>> reviews = latestReviews.stream()
                .map(review -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("rating", review.getRating());
                    map.put("content", review.getContent());
                    map.put("createdAt", review.getCreatedAt());
                    return map;
                })
                .toList();

        return PlaceDetailResponse.builder()
                .placeId(place.getId())
                .name(place.getName())
                .description(place.getDescription())
                .category(place.getCategory().toString())
                .location(roadAddress)
                .start_time(startTime)
                .close_time(endTime)
                .holiday(String.join(", ", holidays))
                .is_parking(place.getIsParking())
                .weather_type(place.getWeatherType() != null ? place.getWeatherType().toString() : null)
                .weight_limit(place.getWeightLimit() != null ? place.getWeightLimit() : 0)
                .pet_fee(place.getPetFee() != null ? place.getPetFee() : 0)
//                .rating(rating)
                .review_count(reviewCount)
                .reviews(reviews)
                .build();
    }

    public PlaceListResponse searchPlaces(SearchCriteria criteria, Pageable pageable) {
        return placeQueryRepository.searchPlaces(criteria, pageable);
    }


    public Page<PopularPlaceResponse> getPopularPlaces(String sort, Category category, Pageable pageable) {
        return placeQueryRepository.findPopularPlaces(sort, category, pageable);
    }

    public List<PopularPlaceResponse> getPopularPlacesByGenderAndAge(Long memberId) {

        // 로그인된 사용자 정보로 성별 및 연령대 조회
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
        Gender gender = member.getGender();
        int age = LocalDate.now().getYear() - member.getBirthDate().getYear();
        int ageGroup = (age / 10) * 10;

        // 인기 장소 조회
        return placeQueryRepository.getPopularPlacesByGenderAndAge(gender, ageGroup);

    }
}