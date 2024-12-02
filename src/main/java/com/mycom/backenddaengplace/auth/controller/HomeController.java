package com.mycom.backenddaengplace.auth.controller;

import com.mycom.backenddaengplace.auth.repository.AuthMemberRepository;
import com.mycom.backenddaengplace.member.domain.Member;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users") // API 엔드포인트 기본 경로 설정
public class HomeController {

    private final AuthMemberRepository authMemberRepository;

    public HomeController(AuthMemberRepository authMemberRepository) {
        this.authMemberRepository = authMemberRepository;
    }

    // 모든 유저 조회
    @GetMapping
    public List<Member> getAllUsers() {
        return authMemberRepository.findAll();
    }

    // 특정 유저 조회 by ID
    @GetMapping("/{id}")
    public Member getUserById(@PathVariable Long id) {
        Optional<Member> user = authMemberRepository.findById(id);
        return user.orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    // 특정 유저 조회 by Email
    @GetMapping("/email/{email}")
    public Member getUserByEmail(@PathVariable String email) {
        Optional<Member> user = authMemberRepository.findAll().stream()
                .filter(member -> email.equals(member.getEmail()))
                .findFirst();
        return user.orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

}
