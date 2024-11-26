package com.mycom.backenddaengplace.member.repository;

import com.mycom.backenddaengplace.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
