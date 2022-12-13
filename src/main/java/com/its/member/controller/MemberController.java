package com.its.member.controller;

import com.its.member.dto.MemberDTO;
import com.its.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/save")
    public String saveForm() {
        return "memberPages/memberSave";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO) {
        memberService.save(memberDTO);
        return "memberPages/memberLogin";
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam (value = "redirectURL",defaultValue = "/member/main")String redirectURL, Model model){  // 디폴트 값은 로그인을 했으면 어떤주소로 보여줄꺼냐

        model.addAttribute("redirectURL",redirectURL);
        return "memberPages/memberLogin";
    }
    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO , HttpSession session,
                        @RequestParam (value = "redirectURL",defaultValue = "/member/main")String redirectURL, Model model){
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null){
            session.setAttribute("loginEmail",memberDTO.getMemberEmail());
            //인터셉터에 걸려서 고르기인한 사용자가 직전에 요청한 페이지로 보내주기 위해서 redirect:/ 직전요청주소
            //인터셉터 걸리지 않고 로그인을 하는 사용자는 defaultValue에 의해서 main으로
          return "redirect:"+redirectURL;
//            return "memberPages/memberMain";
        }else{
            return "memberPages/memberLogin";
        }
    }
    @GetMapping("/main")
    public String mainPage(){
        return "memberPages/memberMain";
    }

    @GetMapping("/")
    public String findAll(Model model){
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList",memberDTOList);
        return "memberPages/members";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model){
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member",memberDTO);
        return "memberPages/detail";
    }

    @GetMapping("/update")
    public String updateForm(Model model, HttpSession session){
        String loginEmail = (String) session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.findByMemberEmail(loginEmail);
        model.addAttribute("member",memberDTO);
        return "memberPages/memberUpdate";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute MemberDTO memberDTO){
        memberService.update(memberDTO);
        return "memberPages/memberMain";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        memberService.delete(id);
        return "redirect:/member/";
    }






    @GetMapping("/detail-axios/{id}")
    public ResponseEntity  findByIdAxios(@PathVariable Long id){ // 스크립트에서 값을 같이 주게되면 파스베이어블 사용
        MemberDTO memberDTO = memberService.findById(id);
        if (memberDTO != null){
            return new ResponseEntity<>(memberDTO,HttpStatus.OK); //상태가 200일때 DTO 객체를 넘긴다
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }
     /*
        get: /member/{id} (조회)
        post:/member/{id} (생성)
        delete:/member/{id} (삭제)
        put:/member/{id} (수정)
      */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteByAxios(@PathVariable Long id){ // 제이슨 타입으로 서버로 주면 프론트 호환성이 좋음
        memberService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateByAxios(@PathVariable Long id,
                                        @RequestBody MemberDTO memberDTO){
        memberService.update(memberDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}


