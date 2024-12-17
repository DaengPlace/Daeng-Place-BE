package com.mycom.backenddaengplace.place.repository;

import com.mycom.backenddaengplace.favorite.domain.QFavorite;
import com.mycom.backenddaengplace.member.domain.QMember;
import com.mycom.backenddaengplace.member.enums.Gender;
import com.mycom.backenddaengplace.place.domain.QLocation;
import com.mycom.backenddaengplace.place.domain.QOperationHour;
import com.mycom.backenddaengplace.place.domain.QPlace;
import com.mycom.backenddaengplace.place.dto.response.*;
import com.mycom.backenddaengplace.place.dto.request.SearchCriteria;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class PlaceQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public PlaceListResponse searchPlaces(SearchCriteria criteria, Long memberId) {
        QPlace place = QPlace.place;
        QOperationHour operationHour = QOperationHour.operationHour;
        QLocation location = QLocation.location;
        QReview review = QReview.review;
        QFavorite favorite = QFavorite.favorite;

        BooleanBuilder whereClause = new BooleanBuilder();

        String todayName = LocalDateTime.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);
        LocalTime now = LocalTime.now();
        if (criteria.getIsOpen() != null && criteria.getIsOpen()) {
            switch (todayName) {
                case "월요일":
                    whereClause.and(
                            operationHour.mondayOpen.isNotNull()
                                    .and(operationHour.mondayOpen.loe(now))
                                    .and(operationHour.mondayClose.goe(now)));
                    break;
                case "화요일":
                    whereClause.and(
                            operationHour.tuesdayOpen.isNotNull()
                                    .and(operationHour.tuesdayOpen.loe(now))
                                    .and(operationHour.tuesdayClose.goe(now)));
                    break;
                case "수요일":
                    whereClause.and(
                            operationHour.wednesdayOpen.isNotNull()
                                    .and(operationHour.wednesdayOpen.loe(now))
                                    .and(operationHour.wednesdayClose.goe(now)));
                    break;
                case "목요일":
                    whereClause.and(
                            operationHour.thursdayOpen.isNotNull()
                                    .and(operationHour.thursdayOpen.loe(now))
                                    .and(operationHour.thursdayClose.goe(now)));
                    break;
                case "금요일":
                    whereClause.and(
                            operationHour.fridayOpen.isNotNull()
                                    .and(operationHour.fridayOpen.loe(now))
                                    .and(operationHour.fridayClose.goe(now)));
                    break;
                case "토요일":
                    whereClause.and(
                            operationHour.saturdayOpen.isNotNull()
                                    .and(operationHour.saturdayOpen.loe(now))
                                    .and(operationHour.saturdayClose.goe(now)));
                    break;
                case "일요일":
                    whereClause.and(
                            operationHour.sundayOpen.isNotNull()
                                    .and(operationHour.sundayOpen.loe(now))
                                    .and(operationHour.sundayClose.goe(now)));
                    break;
            }
        }

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
            whereClause.and(place.operationStatus.eq(criteria.getOperationStatus())); // 운영 상태 검색
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
        OrderSpecifier<?> orderSpecifier = getSortOrderForSearch(
                criteria.getSortBy(),
                place,
                location,
                criteria.getLatitude(),
                criteria.getLongitude()
        );

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
                        review.count().as("review_count"),
                        Expressions.asBoolean(
                                JPAExpressions.selectOne()
                                        .from(favorite)
                                        .where(favorite.member.id.eq(memberId)
                                                .and(favorite.place.id.eq(place.id)))
                                        .exists()
                        ).as("is_favorite")
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
                .fetch();

        List<Long> placeIds = places.stream()
                .map(PlaceDto::getPlaceId)
                .collect(Collectors.toList());
        List<TodayOperationHourDto> operationHours = getTodayOperationHours(placeIds);

        for (PlaceDto placeDto : places) {
            operationHours.stream()
                    .filter(dto -> dto.getPlaceId().equals(placeDto.getPlaceId()))
                    .findFirst()
                    .ifPresent(dto -> {
                        if (placeDto.getOperationHour() == null) {
                            placeDto.setOperationHour(new TodayOperationHourDto());
                        }
                        placeDto.getOperationHour().setTodayOpen(dto.getTodayOpen());
                        placeDto.getOperationHour().setTodayClose(dto.getTodayClose());
                    });
        }

        return PlaceListResponse.builder()
                .places(places)
                .build();
    }

    public List<TodayOperationHourDto> getTodayOperationHours(List<Long> placeIds) {
        QOperationHour operationHour = QOperationHour.operationHour;
        String todayName = LocalDateTime.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);

        // 오늘의 운영 시간 DTO 리스트
        List<TodayOperationHourDto> operationHours = new ArrayList<>();

        for (Long placeId : placeIds) {
            TodayOperationHourDto dto = new TodayOperationHourDto();
            dto.setPlaceId(placeId);

            switch (todayName) {
                case "월요일":
                    LocalTime mondayOpen = jpaQueryFactory
                            .select(operationHour.mondayOpen)
                            .from(operationHour)
                            .where(operationHour.place.id.eq(placeId))
                            .fetchOne();
                    LocalTime mondayClose = jpaQueryFactory
                            .select(operationHour.mondayClose)
                            .from(operationHour)
                            .where(operationHour.place.id.eq(placeId))
                            .fetchOne();
                    dto.setTodayOpen(mondayOpen);
                    dto.setTodayClose(mondayClose);
                    break;
                case "화요일":
                    LocalTime tuesdayOpen = jpaQueryFactory
                            .select(operationHour.tuesdayOpen)
                            .from(operationHour)
                            .where(operationHour.place.id.eq(placeId))
                            .fetchOne();
                    LocalTime tuesdayClose = jpaQueryFactory
                            .select(operationHour.tuesdayClose)
                            .from(operationHour)
                            .where(operationHour.place.id.eq(placeId))
                            .fetchOne();
                    dto.setTodayOpen(tuesdayOpen);
                    dto.setTodayClose(tuesdayClose);
                    break;
                case "수요일":
                    LocalTime wednesdayOpen = jpaQueryFactory
                            .select(operationHour.wednesdayOpen)
                            .from(operationHour)
                            .where(operationHour.place.id.eq(placeId))
                            .fetchOne();
                    LocalTime wednesdayClose = jpaQueryFactory
                            .select(operationHour.wednesdayClose)
                            .from(operationHour)
                            .where(operationHour.place.id.eq(placeId))
                            .fetchOne();
                    dto.setTodayOpen(wednesdayOpen);
                    dto.setTodayClose(wednesdayClose);
                    break;
                case "목요일":
                    LocalTime thursdayOpen = jpaQueryFactory
                            .select(operationHour.thursdayOpen)
                            .from(operationHour)
                            .where(operationHour.place.id.eq(placeId))
                            .fetchOne();
                    LocalTime thursdayClose = jpaQueryFactory
                            .select(operationHour.thursdayClose)
                            .from(operationHour)
                            .where(operationHour.place.id.eq(placeId))
                            .fetchOne();
                    dto.setTodayOpen(thursdayOpen);
                    dto.setTodayClose(thursdayClose);
                    break;
                case "금요일":
                    LocalTime fridayOpen = jpaQueryFactory
                            .select(operationHour.fridayOpen)
                            .from(operationHour)
                            .where(operationHour.place.id.eq(placeId))
                            .fetchOne();
                    LocalTime fridayClose = jpaQueryFactory
                            .select(operationHour.fridayClose)
                            .from(operationHour)
                            .where(operationHour.place.id.eq(placeId))
                            .fetchOne();
                    dto.setTodayOpen(fridayOpen);
                    dto.setTodayClose(fridayClose);
                    break;
                case "토요일":
                    LocalTime saturdayOpen = jpaQueryFactory
                            .select(operationHour.saturdayOpen)
                            .from(operationHour)
                            .where(operationHour.place.id.eq(placeId))
                            .fetchOne();
                    LocalTime saturdayClose = jpaQueryFactory
                            .select(operationHour.saturdayClose)
                            .from(operationHour)
                            .where(operationHour.place.id.eq(placeId))
                            .fetchOne();
                    dto.setTodayOpen(saturdayOpen);
                    dto.setTodayClose(saturdayClose);
                    break;
                case "일요일":
                    LocalTime sundayOpen = jpaQueryFactory
                            .select(operationHour.sundayOpen)
                            .from(operationHour)
                            .where(operationHour.place.id.eq(placeId))
                            .fetchOne();
                    LocalTime sundayClose = jpaQueryFactory
                            .select(operationHour.sundayClose)
                            .from(operationHour)
                            .where(operationHour.place.id.eq(placeId))
                            .fetchOne();
                    dto.setTodayOpen(sundayOpen);
                    dto.setTodayClose(sundayClose);
                    break;
            }
            operationHours.add(dto);
        }

        return operationHours;
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

    private OrderSpecifier<?> getSortOrderForSearch(String sortBy, QPlace place, QLocation location, Double userLat, Double userLon) {
        if (sortBy == null) {
            return Expressions.numberTemplate(Double.class,
                    "ST_Distance_Sphere(Point({0}, {1}), Point({2}, {3}))",
                    place.address.location.longitude, place.address.location.latitude, userLon, userLat).asc();
        }

        return switch (sortBy) {
            case "name" -> place.name.asc();
            case "rating" -> QReview.review.rating.avg().desc();
            case "review" -> QReview.review.count().desc();
            default -> Expressions.numberTemplate(Double.class,
                            "ST_Distance_Sphere(Point({0}, {1}), Point({2}, {3}))",
                            location.longitude, location.latitude, userLon, userLat)
                    .asc();
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
                .limit(5)
                .fetch();

        return content;
    }

    public List<PopularPlaceResponse> findPopularCafes(String sort) {
        QPlace place = QPlace.place;
        QReview review = QReview.review;

        return jpaQueryFactory
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
                .where(
                        place.category.eq(Category.카페)
                        .and(place.outside.isTrue())
                )
                .groupBy(place.id, place.category, place.name)
                .orderBy(getSortOrderForPopular(sort))
                .limit(5)
                .fetch();
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