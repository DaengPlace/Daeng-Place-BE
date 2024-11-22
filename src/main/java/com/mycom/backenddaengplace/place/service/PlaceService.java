package com.mycom.backenddaengplace.place.service;

import com.mycom.backenddaengplace.place.domain.Address;
import com.mycom.backenddaengplace.place.domain.OperationHour;
import com.mycom.backenddaengplace.place.domain.Place;
import com.mycom.backenddaengplace.place.dto.response.PlaceDetailResponse;
import com.mycom.backenddaengplace.place.repository.OperationHourRepository;
import com.mycom.backenddaengplace.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final OperationHourRepository operationHourRepository;

    @Transactional
    public PlaceDetailResponse getPlaceDetail(Long placeId) {

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf("장소를 찾을 수 없습니다! placeId: " + placeId)));

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
                .build();
    }
}
