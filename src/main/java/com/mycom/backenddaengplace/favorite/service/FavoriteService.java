package com.mycom.backenddaengplace.favorite.service;

import com.mycom.backenddaengplace.favorite.domain.Favorite;
import com.mycom.backenddaengplace.favorite.dto.request.FavoriteRequest;
import com.mycom.backenddaengplace.favorite.dto.response.FavoriteRegisterResponse;
import com.mycom.backenddaengplace.favorite.dto.response.FavoritesResponse;
import com.mycom.backenddaengplace.favorite.exception.FavoriteAlreadyExistException;
import com.mycom.backenddaengplace.favorite.exception.FavoriteNotFoundException;
import com.mycom.backenddaengplace.favorite.repository.FavoriteRepository;
import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.member.exception.MemberNotFoundException;
import com.mycom.backenddaengplace.member.repository.MemberRepository;
import com.mycom.backenddaengplace.place.domain.OperationHour;
import com.mycom.backenddaengplace.place.domain.Place;
import com.mycom.backenddaengplace.place.dto.response.OperationHourDto;
import com.mycom.backenddaengplace.place.dto.response.PlaceDto;
import com.mycom.backenddaengplace.place.dto.response.TodayOperationHourDto;
import com.mycom.backenddaengplace.place.exception.PlaceNotFoundException;
import com.mycom.backenddaengplace.place.repository.OperationHourRepository;
import com.mycom.backenddaengplace.place.repository.PlaceRepository;
import com.mycom.backenddaengplace.review.domain.Review;
import com.mycom.backenddaengplace.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final PlaceRepository placeRepository;
    private final ReviewRepository reviewRepository;
    private final OperationHourRepository operationHourRepository;

    @Transactional
    public FavoriteRegisterResponse registerFavorite(FavoriteRequest request, Long memberId) {
        Member registerMember = findMemberById(memberId);
        Place place = findPlaceById(request.getPlaceId());

        return FavoriteRegisterResponse.from(handleRegisterFavorite(registerMember, place));
    }

    @Transactional
    public void deleteFavorite(FavoriteRequest request, Member member) {
        Member deleteMember = findMemberById(member.getId());
        Place place = findPlaceById(request.getPlaceId());
        handleDeleteFavorite(deleteMember, place);

    }

    public FavoritesResponse getFavoritesByMember(Long memberId) {

        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException(memberId);
        }

        List<Favorite> memberFavorites = favoriteRepository.findFavoritesByMemberId(memberId);

        List<PlaceDto> places = new ArrayList<>();

        for (Favorite favorite : memberFavorites) {
            Place place = favorite.getPlace();

            Double rating = reviewRepository.findAverageRatingByPlaceId(place.getId());

            OperationHour operationHour = operationHourRepository.findByPlaceId(place.getId());

            TodayOperationHourDto todayOperationHourDto = new TodayOperationHourDto();
            todayOperationHourDto.setPlaceId(place.getId());

            Long reviewCount = reviewRepository.countByPlaceId(place.getId());

            String todayName = LocalDateTime.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);

            switch (todayName) {
                case "월요일":
                    todayOperationHourDto.setTodayOpen(operationHour.getMondayOpen());
                    todayOperationHourDto.setTodayClose(operationHour.getMondayClose());
                    break;
                case "화요일":
                    todayOperationHourDto.setTodayOpen(operationHour.getTuesdayOpen());
                    todayOperationHourDto.setTodayClose(operationHour.getTuesdayClose());
                    break;
                case "수요일":
                    todayOperationHourDto.setTodayOpen(operationHour.getWednesdayOpen());
                    todayOperationHourDto.setTodayClose(operationHour.getWednesdayClose());
                    break;
                case "목요일":
                    todayOperationHourDto.setTodayOpen(operationHour.getThursdayOpen());
                    todayOperationHourDto.setTodayClose(operationHour.getThursdayClose());
                    break;
                case "금요일":
                    todayOperationHourDto.setTodayOpen(operationHour.getFridayOpen());
                    todayOperationHourDto.setTodayClose(operationHour.getFridayClose());
                    break;
                case "토요일":
                    todayOperationHourDto.setTodayOpen(operationHour.getSaturdayOpen());
                    todayOperationHourDto.setTodayClose(operationHour.getSaturdayClose());
                    break;
                case "일요일":
                    todayOperationHourDto.setTodayOpen(operationHour.getSundayOpen());
                    todayOperationHourDto.setTodayClose(operationHour.getSundayClose());
                    break;
            }

            PlaceDto placeDto = new PlaceDto();
            placeDto.setPlaceId(place.getId());
            placeDto.setName(place.getName());
            placeDto.setPhone(place.getPhone());
            placeDto.setDescription(place.getDescription());
            placeDto.setCategory(place.getCategory());
            placeDto.setLocation(place.getAddress().getRoadAddress());
            placeDto.setIs_parking(place.getIsParking());
            placeDto.setInside(place.getInside());
            placeDto.setOutside(place.getOutside());
            placeDto.setWeight_limit(place.getWeightLimit());
            placeDto.setPet_fee(place.getPetFee());
            placeDto.setLatitude(place.getAddress().getLocation().getLatitude());
            placeDto.setLongitude(place.getAddress().getLocation().getLongitude());
            placeDto.setRating(rating);
            placeDto.setReview_count(reviewCount);
            placeDto.setOperationHour(todayOperationHourDto);

            places.add(placeDto);
        }

        return FavoritesResponse.from(memberId, places);
    }

    private Favorite handleRegisterFavorite(Member member, Place place) {
        Optional<Favorite> existFavorite = favoriteRepository.findByMemberIdAndPlaceId(member.getId(), place.getId());
        if (existFavorite.isPresent()) {
            throw new FavoriteAlreadyExistException(existFavorite.get().getId());
        } else {
            return favoriteRepository.save(Favorite.builder().member(member).place(place).build());
        }
    }

    private void handleDeleteFavorite(Member member, Place place) {
        Optional<Favorite> existFavorite = favoriteRepository.findByMemberIdAndPlaceId(member.getId(), place.getId());
        if (existFavorite.isPresent()) {
            favoriteRepository.deleteById(existFavorite.get().getId());
        } else {
            throw new FavoriteNotFoundException(member.getId(), place.getId());
        }
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() ->
                new MemberNotFoundException(memberId));
    }

    private Place findPlaceById(Long placeId) {
        return placeRepository.findById(placeId).orElseThrow(() ->
                new PlaceNotFoundException(placeId));
    }


}
