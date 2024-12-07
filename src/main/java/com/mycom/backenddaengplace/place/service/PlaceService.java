package com.mycom.backenddaengplace.place.service;

import com.mycom.backenddaengplace.member.enums.Gender;
import com.mycom.backenddaengplace.member.exception.MemberNotFoundException;
import com.mycom.backenddaengplace.member.repository.MemberRepository;
import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.place.domain.Address;
import com.mycom.backenddaengplace.place.domain.OperationHour;
import com.mycom.backenddaengplace.place.domain.Place;
import com.mycom.backenddaengplace.place.dto.request.SearchCriteria;
import com.mycom.backenddaengplace.place.dto.response.*;
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

        Address address = place.getAddress();
        String roadAddress = address != null ? address.getRoadAddress() : null;

        long reviewCount = reviewRepository.countByPlaceId(placeId);
        Double averageRating = reviewRepository.findAverageRatingByPlaceId(placeId);
        List<Review> latestReviews = reviewRepository.findTop3ByPlaceIdOrderByCreatedAtDesc(placeId);

        List<Map<String, Object>> reviews = latestReviews.stream()
                .map(review -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("reviewId", review.getId());
                    map.put("rating", review.getRating());
                    map.put("content", review.getContent());
                    map.put("createdAt", review.getCreatedAt());
                    return map;
                })
                .toList();

        OperationHour operationHour = operationHourRepository.findByPlaceId(placeId);
        String holiday = getHolidayInfo(operationHour);

        // OperationHourDto 생성
        OperationHourDto operationHourDto = new OperationHourDto();
        operationHourDto.setPlaceId(placeId);
        operationHourDto.setMondayOpen(operationHour.getMondayOpen());
        operationHourDto.setMondayClose(operationHour.getMondayClose());
        operationHourDto.setTuesdayOpen(operationHour.getTuesdayOpen());
        operationHourDto.setTuesdayClose(operationHour.getTuesdayClose());
        operationHourDto.setWednesdayOpen(operationHour.getWednesdayOpen());
        operationHourDto.setWednesdayClose(operationHour.getWednesdayClose());
        operationHourDto.setThursdayOpen(operationHour.getThursdayOpen());
        operationHourDto.setThursdayClose(operationHour.getThursdayClose());
        operationHourDto.setFridayOpen(operationHour.getFridayOpen());
        operationHourDto.setFridayClose(operationHour.getFridayClose());
        operationHourDto.setSaturdayOpen(operationHour.getSaturdayOpen());
        operationHourDto.setSaturdayClose(operationHour.getSaturdayClose());
        operationHourDto.setSundayOpen(operationHour.getSundayOpen());
        operationHourDto.setSundayClose(operationHour.getSundayClose());

        return PlaceDetailResponse.builder()
                .placeId(place.getId())
                .name(place.getName())
                .description(place.getDescription())
                .category(place.getCategory().toString())
                .location(roadAddress)
                .is_parking(place.getIsParking())
                .inside(place.getInside())
                .outside(place.getOutside())
                .weight_limit(place.getWeightLimit() != null ? place.getWeightLimit() : 0)
                .pet_fee(place.getPetFee() != null ? place.getPetFee() : 0)
                .homepage(place.getHomepage() != null ? place.getHomepage() : null)
                .operationStatus(place.getOperationStatus())
                .operationHour(operationHourDto)
                .hoilday(holiday)
                .rating(averageRating)
                .review_count(reviewCount)
                .reviews(reviews)
                .build();
    }

    private String getHolidayInfo(OperationHour operationHour) {
        List<String> holidays = new ArrayList<>();

        if (operationHour.getMondayOpen() == null) holidays.add("월요일");
        if (operationHour.getTuesdayOpen() == null) holidays.add("화요일");
        if (operationHour.getWednesdayOpen() == null) holidays.add("수요일");
        if (operationHour.getThursdayOpen() == null) holidays.add("목요일");
        if (operationHour.getFridayOpen() == null) holidays.add("금요일");
        if (operationHour.getSaturdayOpen() == null) holidays.add("토요일");
        if (operationHour.getSundayOpen() == null) holidays.add("일요일");

        // 휴무일 정보 리스트를 문자열로 반환
        return String.join(", ", holidays);
    }

    public PlaceListResponse searchPlaces(SearchCriteria criteria, Pageable pageable) {
        return placeQueryRepository.searchPlaces(criteria, pageable);
    }


    public Page<PopularPlaceResponse> getPopularPlaces(String sort, Category category, Pageable pageable) {
        return placeQueryRepository.findPopularPlaces(sort, category, pageable);
    }

    public AgeGenderPlaceResponse getPopularPlacesByGenderAndAge(Long memberId) {

        // 로그인된 사용자 정보로 성별 및 연령대 조회
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
        Gender gender = member.getGender();
        int age = LocalDate.now().getYear() - member.getBirthDate().getYear();
        int ageGroup = (age / 10) * 10;

        // 인기 장소 조회
        List<PopularPlaceResponse> places =  placeQueryRepository.getPopularPlacesByGenderAndAge(gender, ageGroup);
        AgeGenderPlaceResponse response = new AgeGenderPlaceResponse();
        response.setAge(ageGroup + "대");
        response.setGender(convertGenderToKorean(gender));
        response.setPopularPlaces(places);
        return response;
    }
    private String convertGenderToKorean(Gender gender) {
        return switch (gender) {
            case MALE -> "남성";
            case FEMALE -> "여성";
            default -> "기타";
        };
    }
}