package com.mycom.backenddaengplace.place.repository;

import com.mycom.backenddaengplace.member.domain.QMember;
import com.mycom.backenddaengplace.member.enums.Gender;
import com.mycom.backenddaengplace.place.domain.Place;
import com.mycom.backenddaengplace.place.domain.QLocation;
import com.mycom.backenddaengplace.place.domain.QOperationHour;
import com.mycom.backenddaengplace.place.domain.QPlace;
import com.mycom.backenddaengplace.place.dto.response.OperationHourDto;
import com.mycom.backenddaengplace.place.dto.request.SearchCriteria;
import com.mycom.backenddaengplace.place.dto.response.PlaceDto;
import com.mycom.backenddaengplace.place.dto.response.PlaceListResponse;
import com.mycom.backenddaengplace.place.dto.response.PopularPlaceResponse;
import com.mycom.backenddaengplace.place.enums.Category;
import com.mycom.backenddaengplace.review.domain.QReview;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
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
        QReview review = QReview.review;

        BooleanBuilder whereClause = new BooleanBuilder();

        String todayName = LocalDateTime.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);
        LocalTime now = LocalTime.now();
        Boolean isOpen = false;
//        switch (todayName) {
//            case "월요일": {
//                if (place.operationHour.mondayOpen != null) {
//                    if (place.operationHour.mondayOpen.loe(now)) {
//
//                    } else {
//                        isOpen = false;
//                    }
//                } else {
//                    isOpen = false;
//                }
//            }
//            case "화요일": {
//
//            }
//        }


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
        if (criteria.getPetFee() != null) {
            whereClause.and(place.petFee.eq(criteria.getPetFee())); // 동반 요금 여부 검색
        }
        if (criteria.getWeightLimit() != null) {
            whereClause.and(place.weightLimit.eq(criteria.getWeightLimit())); // 무게 제한 여부 검색
        }
        if (criteria.getInside() != null) {
            whereClause.and(place.inside.eq(criteria.getInside())); // 실내 여부 검색
        }
        if (criteria.getOutside() != null) {
            whereClause.and(place.outside.eq(criteria.getOutside())); // 실외 여부 검색
        }
        if (criteria.getOperationStatus() != null) {
            whereClause.and(place.operationStatus.eq(criteria.getOperationStatus())); // 주차 여부 검색
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
        OrderSpecifier<?> orderSpecifier = getSortOrderForSearch(criteria.getSortBy(), place);

        // 장소 조회 쿼리
        List<PlaceDto> places = jpaQueryFactory
                .select(Projections.bean(PlaceDto.class,
                        place.id.as("placeId"),
                        place.name,
                        place.phone.as("phone"),
                        place.description.as("description"),
                        place.category,
                        place.address.roadAddress.as("location"),
                        place.isParking.as("is_parking"),
                        place.inside.as("inside"),
                        place.outside.as("outside"),
                        place.weightLimit.as("weight_limit"),
                        place.petFee.as("pet_fee"),
                        place.address.location.latitude.as("latitude"),
                        place.address.location.longitude.as("longitude"),
                        Expressions.numberTemplate(Double.class,
                                "ROUND({0}, 1)", review.rating.avg()).as("rating"),
                        review.count().as("review_count")
                ))
                .from(place)
                .leftJoin(place.address)
                .leftJoin(place.address.location, location)
                .leftJoin(review).on(review.place.eq(place))
                .where(whereClause)
                .groupBy(place.id, place.name, place.category, place.address.roadAddress,
                        place.isParking, place.inside, place.outside, place.weightLimit, place.petFee,
                        place.address.location.latitude, place.address.location.longitude)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        OperationHourDto operationHourDto = jpaQueryFactory
                .select(Projections.bean(OperationHourDto.class,
                        operationHour.place.id.as("placeId"),
                        operationHour.mondayOpen,
                        operationHour.mondayClose,
                        operationHour.tuesdayOpen,
                        operationHour.tuesdayClose,
                        operationHour.wednesdayOpen,
                        operationHour.wednesdayClose,
                        operationHour.thursdayOpen,
                        operationHour.thursdayClose,
                        operationHour.fridayOpen,
                        operationHour.fridayClose,
                        operationHour.saturdayOpen,
                        operationHour.saturdayClose,
                        operationHour.sundayOpen,
                        operationHour.sundayClose
                ))
                .from(operationHour)
                .where(operationHour.place.id.eq(
                                place.id
                        )
                ).fetchOne();
        // 운영시간 설정
        places.forEach(placeDto ->
                placeDto.setOperationHour(operationHourDto)
        );
//        places.forEach(placeDto ->
//                placeDto.setOperationHours(operationHourMap.get(placeDto.getPlaceId()))
//        );

        // 마지막 페이지 여부
        boolean isLast = places.size() < pageable.getPageSize();

        return PlaceListResponse.builder()
                .places(places)
                .isLast(isLast)
                .build();
    }

    public Page<PopularPlaceResponse> findPopularPlaces(String sort, Category category, Pageable pageable) {
        QPlace place = QPlace.place;
        QReview review = QReview.review;

        List<PopularPlaceResponse> content = jpaQueryFactory
                .select(Projections.bean(PopularPlaceResponse.class,
                        place.category.stringValue().as("type"),
                        place.id.as("placeId"),
                        place.name,
                        Expressions.numberTemplate(Double.class,
                                "ROUND({0}, 1)", review.rating.avg()).as("rating"),
                        review.rating.avg().multiply(0.7)
                                .add(review.count().castToNum(Double.class).multiply(0.3))
                                .as("popularityScore")
                ))
                .from(place)
                .leftJoin(review).on(review.place.eq(place))
                .where(categoryEq(category))
                .groupBy(place.id, place.category, place.name)
                .orderBy(getSortOrderForPopular(sort))
                .offset(pageable.getOffset())
                .limit(3)
                .fetch();

        long total = getTotal(category);

        return PageableExecutionUtils.getPage(content, pageable, () -> total);
    }

    private OrderSpecifier<?> getSortOrderForSearch(String sortBy, QPlace place) {
        if (sortBy == null) {
            return place.name.asc();
        }

        return switch (sortBy) {
            case "name" -> place.name.asc();
            case "rating" -> QReview.review.rating.avg().desc();
            case "review" -> QReview.review.count().desc();
            default -> place.name.asc();
        };
    }

    private BooleanExpression categoryEq(Category category) {
        return category != null ? QPlace.place.category.eq(category) : null;
    }

    private OrderSpecifier<?> getSortOrderForPopular(String sort) {
        QReview review = QReview.review;

        return switch (sort) {
            case "rating" -> review.rating.avg().desc();
            case "review" -> review.count().desc();
            default -> review.rating.avg().multiply(0.7)
                    .add(review.count().castToNum(Double.class).multiply(0.3))
                    .desc();
        };
    }

    private long getTotal(Category category) {
        QPlace place = QPlace.place;
        return jpaQueryFactory
                .select(place.count())
                .from(place)
                .where(categoryEq(category))
                .fetchOne();
    }

    public List<PopularPlaceResponse> getPopularPlacesByGenderAndAge(Gender gender, int age) {
        QPlace place = QPlace.place;
        QReview review = QReview.review;
        QMember member = QMember.member;

        List<PopularPlaceResponse> content = jpaQueryFactory
                .select(Projections.bean(PopularPlaceResponse.class,
                        place.category.stringValue().as("type"),
                        place.id.as("placeId"),
                        place.name,
                        Expressions.numberTemplate(Double.class,
                                "ROUND({0}, 1)", review.rating.avg()).as("rating"),
                        review.rating.avg().multiply(0.7)
                                .add(review.count().castToNum(Double.class).multiply(0.3))
                                .as("popularityScore")
                ))
                .from(place)
                .leftJoin(review).on(review.place.eq(place))
                .leftJoin(member).on(review.member.eq(member))
                .where(
                        member.gender.eq(gender),
                        ageBetween(member.birthDate, age)
                )
                .groupBy(place.id, place.category, place.name)
                .orderBy(getSortOrderForPopular("popularity"))
                .offset(0)
                .limit(3)
                .fetch();

        return content;
    }

    private BooleanExpression ageBetween(DateTimePath<LocalDateTime> birthDate, int targetAge) {
        int currentYear = LocalDateTime.now().getYear();
        int startYear = currentYear - targetAge - 10;
        int endYear = currentYear - targetAge + 1;
        LocalDateTime startDate = LocalDateTime.of(startYear, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(endYear, 12, 31, 23, 59);
        return birthDate.between(startDate, endDate);
    }


}