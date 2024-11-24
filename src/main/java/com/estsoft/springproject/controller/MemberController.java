package com.estsoft.springproject.controller;

import com.estsoft.springproject.entity.Member;
import com.estsoft.springproject.user.config.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public List<Member> getAllMember() {
        return memberService.getAllMembers();
    }

    // HTTP Post Method 요청(/members) 받는 메소드
    @PostMapping("/members")
    public ResponseEntity<Member> saveMember(@RequestBody Member member) {
        // member 정보 저장하는 service 코드 호출
        Member savedMember = memberService.saveMember(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMember);
    }
}