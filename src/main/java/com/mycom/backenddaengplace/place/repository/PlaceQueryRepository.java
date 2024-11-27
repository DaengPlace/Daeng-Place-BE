package com.mycom.backenddaengplace.place.repository;

import com.mycom.backenddaengplace.place.domain.QLocation;
import com.mycom.backenddaengplace.place.domain.QOperationHour;
import com.mycom.backenddaengplace.place.domain.QPlace;
import com.mycom.backenddaengplace.place.dto.response.OperationHourDto;
import com.mycom.backenddaengplace.place.dto.request.SearchCriteria;
import com.mycom.backenddaengplace.place.dto.response.PlaceDto;
import com.mycom.backenddaengplace.place.dto.response.PlaceListResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Repository
public class PlaceQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public PlaceListResponse searchPlaces(SearchCriteria criteria, Pageable pageable) {
        QPlace place = QPlace.place;
        QOperationHour operationHour = QOperationHour.operationHour;
        QLocation location = QLocation.location;

        BooleanBuilder whereClause = new BooleanBuilder();

        // 검색 조건 추가
        if (criteria.getSearch() != null) {
            whereClause.and(place.name.containsIgnoreCase(criteria.getSearch())); // 이름 검색
        }
        if (criteria.getAddress() != null) {
            whereClause.and(place.address.roadAddress.containsIgnoreCase(criteria.getAddress())); // 주소 검색
        }
        if (criteria.getCategory() != null) {
            whereClause.and(place.category.eq(criteria.getCategory())); // 카테고리 검색
        }
        if (criteria.getIsParking() != null) {
            whereClause.and(place.isParking.eq(criteria.getIsParking())); // 주차 여부 검색
        }
        if (criteria.getWeatherType() != null) {
            whereClause.and(place.weatherType.eq(criteria.getWeatherType())); // 날씨 유형 검색
        }
        if (criteria.getWeightLimit() != null) {
            whereClause.and(place.weightLimit.loe(criteria.getWeightLimit())); // 무게 제한
        }
        if (criteria.getPetFee() != null) {
            whereClause.and(place.petFee.loe(criteria.getPetFee())); // 애견 요금
        }

        // 위치 기반 검색 (위도, 경도 추가)
        if (criteria.getLatitude() != null && criteria.getLongitude() != null) {
            double userLat = criteria.getLatitude();
            double userLon = criteria.getLongitude();
            double radius = 5000; // 5km

            whereClause.and(
                JPAExpressions
                    .select(Expressions.numberTemplate(Double.class,
                        "ST_Distance_Sphere(Point({0}, {1}), Point({2}, {3}))",
                        location.longitude, location.latitude, userLon, userLat))
                    .from(location)
                    .where(location.address.id.eq(place.address.id))
                    .lt(radius) // 5km 이내만 필터링
            );
        }

        // 정렬 기준 처리 (sortBy)
        OrderSpecifier<?> orderSpecifier = getSortOrder(criteria.getSortBy(), place);

        // 장소 조회 쿼리
        List<PlaceDto> places = jpaQueryFactory
            .select(Projections.bean(PlaceDto.class,
                place.id.as("placeId"),
                place.name,
                place.category,
                place.address.roadAddress.as("location"),
                place.isParking.as("is_parking"),
                place.weatherType.as("weather_type"),
                place.weightLimit.as("weight_limit"),
                place.petFee.as("pet_fee"),
                place.address.location.latitude.as("latitude"),
                place.address.location.longitude.as("longitude")
            ))
            .from(place)
            .leftJoin(place.address)
            .leftJoin(place.address.location, location)
            .where(whereClause)
            .orderBy(orderSpecifier) // 정렬 추가
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        // 운영시간 조회
        Map<Long, List<OperationHourDto>> operationHourMap = jpaQueryFactory
            .select(Projections.bean(OperationHourDto.class,
                operationHour.place.id.as("placeId"),
                operationHour.dayOfWeek,
                operationHour.startTime,
                operationHour.endTime,
                operationHour.isDayOff
            ))
            .from(operationHour)
            .where(operationHour.place.id.in(
                    places.stream().map(PlaceDto::getPlaceId).toList()
            ))
            .fetch()
            .stream()
            .collect(Collectors.groupingBy(OperationHourDto::getPlaceId));

        // 운영시간 설정
        places.forEach(placeDto ->
            placeDto.setOperationHours(operationHourMap.get(placeDto.getPlaceId()))
        );

        // 마지막 페이지 여부
        boolean isLast = places.size() < pageable.getPageSize();

        return PlaceListResponse.builder()
            .places(places)
            .isLast(isLast)
            .build();
    }

    private OrderSpecifier<?> getSortOrder(String sortBy, QPlace place) {
        if (sortBy == null) {
            return place.name.asc(); // 기본값은 이름 기준 오름차순
        }

        // 정렬 예시 -> 추후 별점순 추가 예정
        return switch (sortBy) {
            case "name" -> place.name.asc(); // 이름 기준 오름차순
            default -> place.name.asc();
        };
    }
}
