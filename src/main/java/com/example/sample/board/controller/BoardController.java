package com.example.sample.board.controller;

import com.example.sample.board.dto.BoardDTO;
import com.example.sample.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.sample.board.dto.PageRequestDTO;

@Controller
@RequestMapping({"", "/", "/board", "/board/", "/logout"})
@Log4j2
@RequiredArgsConstructor // 생성자 자동주입
public class BoardController {

    private final BoardService service; // 인터페이스에 필요한 생성자 자동 주입, final 선언 필요

    @GetMapping({""}) // 리퀘스트매핑 자동이동.
    public String index() {
        return "redirect:/board/list";
    }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {
        log.info("list..." + pageRequestDTO);
        model.addAttribute("result", service.getList(pageRequestDTO));
        // 스프링 MVC기능으로 화면에서 PageRequestDTO의 파라미터(page, size) 수집가능.
        // Model은 데이터를 화면에 전달하기 위해서 사용
    }

    @GetMapping("/register")
    public void register() {
        log.info("register get...");
    }

    @PostMapping("/register")
    public String registerPost(BoardDTO dto, RedirectAttributes redirectAttributes){
        log.info("dto..." + dto);

        // 새로 추가된 엔티티의 번호
        Long boardNo = service.register(dto);
        redirectAttributes.addFlashAttribute("msg", boardNo);
        return "redirect:/board/list"; // post + redirect

    }

    @GetMapping({"/read", "/modify"})
    public void read(long boardNo, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {
        // @ModelAttribute("requestDTO") 는 키,밸류형태에서 키 이름 지정..
        log.info("boardNo : " + boardNo);

        BoardDTO dto = service.read(boardNo); // entity의 boardNo로 entity를 꺼내서 dto로 바꾼것을 dto에 담아준다.

        model.addAttribute("dto", dto); // entity -> dto가 된 것을 모델에 담아서 전송!

    }

    @PostMapping("/remove")
    public String remove(long boardNo, RedirectAttributes redirectAttributes) {

        log.info("boardNo : " + boardNo);

        service.remove(boardNo);

        redirectAttributes.addFlashAttribute("msg", boardNo);

        return "redirect:/board/list"; // 삭제 후 목록의 첫 페이지로 가는 것이 보편적이다.

    }
    @PostMapping("/modify")
    public String modify(BoardDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO
    , RedirectAttributes redirectAttributes){
        // 수정해야하는 내용을 가지고 있는 dto , 기존의 페이지 정보를 유지하기 위한 PageRequestDTO , ++리다이렉트로 이동을 위한 파라미터

        log.info("post modify.......................................");
        log.info("dto : " + dto);

        service.modify(dto); // 값은 수정된 값을 entity에 저장할 것임.

        // 수정 후에는 기존의 페이지 정보는 그대로 남게끔 검색키워드 type, keyword 파라미터도 추가
        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("boardNo", dto.getBoardNo());

        return "redirect:/board/read";
    }

}
