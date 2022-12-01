package com.its.member;

import com.its.member.dto.MemberDTO;
import com.its.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*; // 어썰트 제이 테스트를 위한 라이브러리 어설션즈 스테틱 매서드를 편하게 쓰기위함

@SpringBootTest
public class MemberTest {
    @Autowired
    private MemberService memberService;

    // 회원가입 테스트
    // 신규회원 데이터 생성(DTO)
    // 회원가입 기능 실행
    // 가입 성공 후 가져온 id 값으로 DB 조회를 하고
    // 가입시 사용한 email과 DB에서 조회한 email이 일치하는지를 판단
    @Test
    @Transactional
    @Rollback(value = true)
    public void memberSaveTest() {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberEmail("testEmail5");
        memberDTO.setMemberPassword("testPassword");
        memberDTO.setMemberName("testName");
        memberDTO.setMemberAge(22);
        memberDTO.setMemberPhone("010-1111-1111");

        Long saveId = memberService.save(memberDTO);

       MemberDTO savedMember = memberService.findById(saveId);
       // 테스트 코드 if문으로 하지 않음 .
        assertThat(memberDTO.getMemberEmail()).isEqualTo(savedMember.getMemberEmail());
         // 테스트로 이메일을 확인 자동적으로 결과로 확인

    }
}