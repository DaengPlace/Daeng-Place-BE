package com.mycom.backenddaengplace.favorite.service;

import com.mycom.backenddaengplace.favorite.domain.Favorite;
import com.mycom.backenddaengplace.favorite.exception.FavoriteAlreadyExistException;
import com.mycom.backenddaengplace.favorite.exception.FavoriteNotFoundException;
import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.member.exception.MemberNotFoundException;
import com.mycom.backenddaengplace.member.repository.MemberRepository;
import com.mycom.backenddaengplace.place.domain.Place;
import com.mycom.backenddaengplace.place.dto.request.FavoriteDeleteRequest;
import com.mycom.backenddaengplace.place.dto.request.FavoriteRegisterRequest;
import com.mycom.backenddaengplace.place.dto.response.FavoriteRegisterResponse;
import com.mycom.backenddaengplace.place.exception.PlaceNotFoundException;
import com.mycom.backenddaengplace.place.repository.FavoriteRepository;
import com.mycom.backenddaengplace.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final PlaceRepository placeRepository;

    public FavoriteRegisterResponse registerFavorite(FavoriteRegisterRequest request) {
        Member member = findMemberById(request.getMemberId());
        Place place = findPlaceById(request.getPlaceId());

        Favorite favorite = handleRegisterFavorite(member, place);


    }

    public void deleteFavorite(FavoriteDeleteRequest request) {
        Member member = findMemberById(request.getMemberId());
        Place place = findPlaceById(request.getPlaceId());

        favoriteRepository.deleteFavoriteById();
    }

    public List<Favorite> getFavoritesByMember(FavoriteRegisterRequest request) {
        Member member = findMemberById(request.getMemberId());

    }

    private Favorite handleRegisterFavorite(Member member, Place place) {
        Optional<Favorite> existFavorite = favoriteRepository.findByMemberIdPlaceId(member.getId(), place.getId());
        if (existFavorite.isPresent()) {
            throw new FavoriteAlreadyExistException(existFavorite.get().getId());
        } else {
            return favoriteRepository
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
