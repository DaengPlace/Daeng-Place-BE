package com.mycom.backenddaengplace.member.repository;

import com.mycom.backenddaengplace.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Member> findByProviderAndProviderId(String provider, String providerId);

    boolean existsByNickname(String nickname);

    List<Member> findByIsDeletedTrueAndDeletedAtBefore(LocalDateTime localDateTime);

    @Modifying
    @Query("DELETE FROM Member m WHERE m.id IN :ids")
    void deleteAllByIds(List<Long> ids);

}
