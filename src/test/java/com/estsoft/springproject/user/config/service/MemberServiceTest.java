package com.estsoft.springproject.user.config.service;

import com.estsoft.springproject.entity.Member;
import com.estsoft.springproject.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;


    @Test
    void testGetAllMembers() {
        // Given
        long memberId1 = 1L;
        long memberId2 = 2L;

        String memberName1 = "member1";
        String memberName2 = "member2";

        Member member1 = new Member(memberId1, memberName1);
        Member member2 = new Member(memberId2, memberName2);
        List<Member> members = Arrays.asList(member1, member2);

        when(memberRepository.findAll()).thenReturn(members);

        // When
        List<Member> result = memberService.getAllMembers();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(member1.getName(), result.get(0).getName());
        assertEquals(member2.getName(), result.get(1).getName());

        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void testSaveMember() {
        // Given
        long memberId = 1L;

        String memberName = "member";

        Member member = new Member(memberId, memberName);
        when(memberRepository.save(member)).thenReturn(member);

        // When
        Member savedMember = memberService.saveMember(member);

        // Then
        assertNotNull(savedMember);
        assertEquals(memberName, savedMember.getName());

        verify(memberRepository, times(1)).save(member);
    }
}
