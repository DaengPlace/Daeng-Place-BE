package com.mycom.backenddaengplace.trait.repository;

import com.mycom.backenddaengplace.trait.domain.MemberTraitResponse;
import com.mycom.backenddaengplace.trait.domain.MemberTraitResponseId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTraitResponseRepository extends JpaRepository<MemberTraitResponse, MemberTraitResponseId> {
}
