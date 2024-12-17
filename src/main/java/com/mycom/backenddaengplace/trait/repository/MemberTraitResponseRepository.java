package com.mycom.backenddaengplace.trait.repository;

import com.mycom.backenddaengplace.trait.domain.MemberTraitResponse;
import com.mycom.backenddaengplace.trait.domain.MemberTraitResponseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberTraitResponseRepository extends JpaRepository<MemberTraitResponse, MemberTraitResponseId> {

    List<MemberTraitResponse> findByMemberId(Long memberId);

    @Modifying
    @Query("DELETE FROM MemberTraitResponse mtr WHERE mtr.member.id = :memberId")
    void deleteByMemberId(Long memberId);
}
