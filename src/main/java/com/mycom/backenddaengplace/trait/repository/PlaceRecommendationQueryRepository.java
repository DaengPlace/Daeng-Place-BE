package com.mycom.backenddaengplace.trait.repository;

import com.mycom.backenddaengplace.place.domain.QPlace;
import com.mycom.backenddaengplace.place.dto.response.PopularPlaceResponse;
import com.mycom.backenddaengplace.place.enums.Category;
import com.mycom.backenddaengplace.review.domain.QReview;
import com.mycom.backenddaengplace.trait.domain.QTraitTag;
import com.mycom.backenddaengplace.trait.domain.QTraitTagCount;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class PlaceRecommendationQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<PopularPlaceResponse> findRecommendedPlaces(Set<Category> categories) {
        QPlace place = QPlace.place;
        QReview review = QReview.review;

        return queryFactory
                .select(Projections.bean(PopularPlaceResponse.class,
                        place.category.stringValue().as("type"),
                        place.id.as("placeId"),
                        place.name,
                        review.rating.avg().as("rating")))
                .from(place)
                .leftJoin(review).on(review.place.eq(place))
                .where(place.category.in(categories))
                .groupBy(place.id, place.name, place.category)
                .orderBy(review.rating.avg().desc())
                .limit(3)
                .fetch();
    }

    public List<PopularPlaceResponse> findRecommendedPlacesForMember(Boolean isCostEffective, Boolean needsParking, Boolean uniquePlaces, Boolean cleanPlaces) {
        QPlace place = QPlace.place;
        QReview review = QReview.review;
        QTraitTag traitTag = QTraitTag.traitTag;
        QTraitTagCount traitTagCount = QTraitTagCount.traitTagCount;

        BooleanBuilder builder = new BooleanBuilder();

        if (needsParking) {
            builder.and(place.isParking.eq(true));
        }

        if (isCostEffective || uniquePlaces || cleanPlaces) {
            List<Long> targetTagIds = new ArrayList<>();
            if (isCostEffective) targetTagIds.add(8L);
            if (uniquePlaces) targetTagIds.add(6L);
            if (cleanPlaces) targetTagIds.add(7L);

            builder.and(traitTag.id.in(targetTagIds));
        }

        return queryFactory
                .select(Projections.constructor(
                        PopularPlaceResponse.class,
                        place.category.stringValue().as("type"),
                        place.id.as("placeId"),
                        place.name.as("name"),
                        review.rating.avg().as("rating"),
                        Expressions.numberTemplate(Double.class,
                                        "({0} * 0.7) + ({1} * 0.3)",
                                        traitTagCount.id.traitTagId.count().doubleValue(), // 태그 수
                                        review.rating.avg()) // 별점
                                .as("popularityScore")
                ))
                .from(traitTagCount)
                .join(traitTagCount.review, review)
                .join(review.place, place)
                .join(traitTagCount.traitTag, traitTag)
                .where(builder)
                .groupBy(place.id, place.name)
                .orderBy(Expressions.numberPath(Double.class, "popularityScore").desc())
                .limit(5)
                .fetch();
    }
}