package com.mycom.backenddaengplace.auth.repository;

import com.mycom.backenddaengplace.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthMemberRepository extends JpaRepository<Member, Long> {
    Member findByProviderAndProviderId(String provider, String providerId);
    boolean existsByEmail(String email);
}
