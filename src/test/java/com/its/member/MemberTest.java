package com.its.member;

import com.its.member.dto.MemberDTO;
import com.its.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
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
    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("로그인테스트")
    public void loginTest(){
        //1. 회원가입
        String loginEmail = "loginEmail";
        String loginPassword = "loginPassword";

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberEmail(loginEmail);
        memberDTO.setMemberPassword(loginPassword);
        memberDTO.setMemberName("loginName");
        memberDTO.setMemberAge(22);
        memberDTO.setMemberPhone("010-1111-1111");
        memberService.save(memberDTO);
        // save 매서드를 복사해서
        //2, 로그인 수행
        MemberDTO loginDTO = new MemberDTO();
        loginDTO.setMemberEmail(loginEmail);     // dto에 이메일과 비밀번호가 있으니까 dto로 이용
        loginDTO.setMemberPassword(loginPassword);
        MemberDTO loginResult = memberService.login(loginDTO);
        //3. 로그인 결과가 null 이 아니면 테스트 통과
        assertThat(loginResult).isNotNull();

    }


    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("업데이트 테스트")
    public void updateTest() {

        MemberDTO memberDTO = newMember(); // 매서드를 호출
        Long savedId = memberService.save(memberDTO); // 가입 절차

        // 수정용 MemberDTO
        memberDTO.setId(savedId);    // 수정하기 위해 id값을 셋팅
        memberDTO.setMemberName("수정이름"); // 수정하고 싶은 값을 셋팅

        // 수정처리
        memberService.update(memberDTO);

        // DB에서 조회한 이름이 수정할 떄 사용한 이름과 같은지 확인
        MemberDTO memberDB = memberService.findById(savedId); // 다시 조회
        assertThat(memberDB.getMemberName()).isEqualTo(memberDTO.getMemberName());


    }

//
//        String loginEmail = "loginEmail";
//        String loginPassword = "loginPassword";
//
//        MemberDTO memberDTO = new MemberDTO();
//        memberDTO.setMemberEmail(memberDTO.getMemberEmail());
//        memberDTO.setMemberPassword(memberDTO.getMemberPassword());
//        memberDTO.setMemberName("loginName");
//        memberDTO.setMemberAge(22);
//        memberDTO.setMemberPhone("010-1111-1111");
//        memberService.update(memberDTO);
//
//        MemberDTO updateDTO = new MemberDTO();
//        updateDTO.setMemberEmail();

//        MemberDTO updateDTO = new MemberDTO();
//    updateDTO.setMemberEmail(Req.getMemberEmail());
//    updateDTO.setMemberPassword(memberDTO.getMemberPassword());
//     MemberDTO updateResult = memberService.update(memberDTO);
//        assertThat(updateResult)

        public MemberDTO newMember(){
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setMemberEmail("testEmail");
            memberDTO.setMemberPassword("testPassword");
            memberDTO.setMemberName("testName");
            memberDTO.setMemberAge(22);
            memberDTO.setMemberPhone("010-1111-1111");

            return memberDTO;
        }

        @Test
        @Transactional
        @Rollback(value = true)
        @DisplayName("탈퇴 테스트")
        public void deleteTest(){

        MemberDTO memberDTO = newMember();
        Long savedId = memberService.save(memberDTO); // 가입

//        memberDTO.setId(savedId);

        memberService.delete(savedId);

//        MemberDTO memberDB =  memberService.findById(savedId);
////        assertThat(memberDB.getId()).isEqualTo(memberDTO.getId());
            assertThat(memberService.findById(savedId)).isNull(); // 조회을 하고 데이타가 없으면 ,null



        }




}