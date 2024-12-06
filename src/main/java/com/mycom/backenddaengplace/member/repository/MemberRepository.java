package com.mycom.backenddaengplace.member.repository;

import com.mycom.backenddaengplace.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);
    Optional<Member> findByProviderAndProviderId(String provider, String providerId);

}
