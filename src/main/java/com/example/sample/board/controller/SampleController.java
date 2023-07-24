package com.example.sample.board.controller;

import com.example.sample.member.dto.ClubAuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sample")
@Log4j2
@RequiredArgsConstructor // 생성자 자동주입
public class SampleController {


    @GetMapping({"/"})
    public String index() {
        return "redirect:/sample/list";
    }

    @GetMapping("/all")
    public void exAll(){
        log.info("로그인을 하지 않아도 접근가능");
    }

    @GetMapping("/member")
    public void exMember(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMember){
        // ClubUserDetailsService의 loadUserByUsername() 쪽으로 받아와서 db랑 대조해보고 같다면 꺼내서 dto로 바꿔서 여기로
        log.info("member login..........");
        // 컨트롤러에서 로그인 된 사용자 정보 확인하기
        log.info("--------------로그인 정보로 DTO를 만들어옵니다.-------------------");
        log.info(clubAuthMember);
    }

    @GetMapping("/admin")
    public void exAdmin(){
        log.info("관리자만 접근가능");
    }


}
