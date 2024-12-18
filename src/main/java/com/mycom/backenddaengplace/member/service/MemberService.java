package com.mycom.backenddaengplace.member.service;

import com.mycom.backenddaengplace.common.service.S3ImageService;
import com.mycom.backenddaengplace.favorite.repository.FavoriteRepository;
import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.member.dto.request.MemberRegisterRequest;
import com.mycom.backenddaengplace.member.dto.request.MemberUpdateRequest;
import com.mycom.backenddaengplace.member.dto.response.BaseMemberResponse;
import com.mycom.backenddaengplace.member.dto.response.DuplicateCheckResponse;
import com.mycom.backenddaengplace.member.exception.MemberNotFoundException;
import com.mycom.backenddaengplace.member.repository.MemberRepository;
import com.mycom.backenddaengplace.pet.exception.InvalidBirthDateException;
import com.mycom.backenddaengplace.pet.repository.PetRepository;
import com.mycom.backenddaengplace.review.repository.MediaFileRepository;
import com.mycom.backenddaengplace.review.repository.ReviewLikeRepository;
import com.mycom.backenddaengplace.review.repository.ReviewRepository;
import com.mycom.backenddaengplace.trait.repository.MemberTraitResponseRepository;
import com.mycom.backenddaengplace.trait.repository.PetTraitResponseRepository;
import com.mycom.backenddaengplace.trait.repository.TraitTagCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final S3ImageService s3ImageService;
    private final MemberRepository memberRepository;
    private final PetRepository petRepository;
    private final PetTraitResponseRepository petTraitResponseRepository;
    private final MemberTraitResponseRepository memberTraitResponseRepository;
    private final FavoriteRepository favoriteRepository;
    private final ReviewRepository reviewRepository;
    private final TraitTagCountRepository traitTagCountRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final MediaFileRepository mediaFileRepository;


    public BaseMemberResponse getMember(Long memberId) {
        return BaseMemberResponse.from(memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId)));
    }

    @Transactional
    public BaseMemberResponse registerMember(MemberRegisterRequest request, MultipartFile file) {
        // 카카오 이메일로 기존 회원 조회
        Member existingMember = memberRepository.findByEmail(request.getEmail())
                .orElse(null);

        String imageUrl = null;
        if (file != null && !file.isEmpty()) {
            imageUrl = s3ImageService.uploadImage(file, S3ImageService.USER_PROFILE_DIR);
        }

        if (existingMember != null) {
            // 기존 회원이 있다면 프로필 정보 업데이트
            return updateExistingMember(existingMember, request, imageUrl);
        } else {
            // 신규 회원이라면 새로 생성
            return createNewMember(request, imageUrl);
        }
    }

    private BaseMemberResponse updateExistingMember(Member existingMember, MemberRegisterRequest request, String imageUrl) {
        LocalDateTime birthDate = parseBirthDate(request.getBirthDate());

        // 기존 이미지가 있고 새 이미지가 업로드된 경우 기존 이미지 삭제
        if (imageUrl != null && existingMember.getProfileImageUrl() != null) {
            s3ImageService.deleteImage(existingMember.getProfileImageUrl());
        }


        existingMember.setName(request.getName());
        existingMember.setNickname(request.getNickname());
        existingMember.setBirthDate(birthDate);
        existingMember.setGender(request.getGender());
        existingMember.setState(request.getState());
        existingMember.setCity(request.getCity());
        if (imageUrl != null) {
            existingMember.setProfileImageUrl(imageUrl);
        }

        Member savedMember = memberRepository.save(existingMember);
        return BaseMemberResponse.from(savedMember);
    }

    private BaseMemberResponse createNewMember(MemberRegisterRequest request, String imageUrl) {
        LocalDateTime birthDate = parseBirthDate(request.getBirthDate());

        Member member = Member.builder()
                .email(request.getEmail())
                .name(request.getName())
                .nickname(request.getNickname())
                .birthDate(birthDate)
                .gender(request.getGender())
                .state(request.getState())
                .city(request.getCity())
                .profileImageUrl(imageUrl)
                .locationStatus(false)
                .build();

        Member savedMember = memberRepository.save(member);
        return BaseMemberResponse.from(savedMember);
    }

    @Transactional
    public BaseMemberResponse reviseMember(MemberUpdateRequest request, MultipartFile file, Long memberId) {
        Member updatedMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        LocalDateTime birthDate = parseBirthDate(request.getBirthDate());

        if (file != null && !file.isEmpty()) {
            log.info("Uploading new file...");
            if (updatedMember.getProfileImageUrl() != null) {
                s3ImageService.deleteImage(updatedMember.getProfileImageUrl());
            }
            String imageUrl = s3ImageService.uploadImage(file, S3ImageService.USER_PROFILE_DIR);
            updatedMember.setProfileImageUrl(imageUrl);
        } else if (request.getProfileImageUrl() != null) {
            updatedMember.setProfileImageUrl(request.getProfileImageUrl());
        }

        updatedMember.setNickname(request.getNickname());
        updatedMember.setGender(request.getGender());
        updatedMember.setState(request.getState());
        updatedMember.setCity(request.getCity());
        updatedMember.setBirthDate(birthDate);
        Member savedMember = memberRepository.save(updatedMember);

        return BaseMemberResponse.from(updatedMember);
    }

    private LocalDateTime parseBirthDate(String birthDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            return LocalDate.parse(birthDate, formatter).atStartOfDay();
        } catch (DateTimeParseException e) {
            throw new InvalidBirthDateException();
        }
    }

    @Transactional
    public BaseMemberResponse logicalDeleteMember(Long memberId) {
        Member deletedMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        deletedMember.setIsDeleted(true);
        deletedMember.setDeletedAt(LocalDateTime.now());
        memberRepository.save(deletedMember);

        return BaseMemberResponse.from(deletedMember);
    }

    @Transactional
    public DuplicateCheckResponse checkDuplicateNickname(String nickname) {
        boolean isValid = memberRepository.existsByNickname(nickname);
        return DuplicateCheckResponse.from(!isValid);
    }

    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void deleteExpiredMembers() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

        List<Member> membersToDelete = memberRepository.findByIsDeletedTrueAndDeletedAtBefore(thirtyDaysAgo);

        if (!membersToDelete.isEmpty()) {
            List<Long> memberIds = membersToDelete.stream().map(Member::getId).toList();
            for (Long memberId : memberIds) {
                memberTraitResponseRepository.deleteByMemberId(memberId);
                favoriteRepository.deleteByMemberId(memberId);
                petRepository.findByMemberId(memberId).forEach(pet -> petTraitResponseRepository.deleteByPetId(pet.getId()));
                petRepository.deleteByMemberId(memberId);

                reviewRepository.findByMemberId(memberId).forEach(review -> {
                            traitTagCountRepository.deleteByReviewId(review.getId());
                            reviewLikeRepository.deleteByReviewId(review.getId());
                            mediaFileRepository.deleteByReviewId(review.getId());
                        }
                );

                reviewLikeRepository.deleteByMemberId(memberId);
                reviewRepository.deleteByMemberId(memberId);
            }
            memberRepository.deleteAllByIds(memberIds);
            System.out.println("Deleted Members " + memberIds.size());
        } else {
            System.out.println("Deleted Members is empty");
        }
    }
}