package com.mycom.backenddaengplace.trait.repository;

import com.mycom.backenddaengplace.place.domain.QPlace;
import com.mycom.backenddaengplace.place.dto.response.PopularPlaceResponse;
import com.mycom.backenddaengplace.place.enums.Category;
import com.mycom.backenddaengplace.review.domain.QReview;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}