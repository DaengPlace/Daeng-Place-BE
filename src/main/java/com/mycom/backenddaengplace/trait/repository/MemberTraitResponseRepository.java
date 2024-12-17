package com.mycom.backenddaengplace.trait.repository;

import com.mycom.backenddaengplace.trait.domain.MemberTraitResponse;
import com.mycom.backenddaengplace.trait.domain.MemberTraitResponseId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberTraitResponseRepository extends JpaRepository<MemberTraitResponse, MemberTraitResponseId> {

    List<MemberTraitResponse> findByMemberId(Long memberId);

    void deleteByMemberId(Long memberId);
}
