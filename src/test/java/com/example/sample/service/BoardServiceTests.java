package com.example.sample.service;

import com.example.sample.board.dto.BoardDTO;
import com.example.sample.board.dto.PageRequestDTO;
import com.example.sample.board.dto.PageResultDTO;
import com.example.sample.board.service.BoardService;
import com.example.sample.board.entity.Board;
import com.example.sample.member.repository.ClubMemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// BoardService가 잘 동작하는지 테스트하기 위해
@SpringBootTest
public class BoardServiceTests {

    private final BoardService service;
    private final ClubMemberRepository clubMemberRepository;

    @Autowired
    public BoardServiceTests(BoardService service, ClubMemberRepository clubMemberRepository) {
        this.service = service;
        this.clubMemberRepository = clubMemberRepository;
    }


    @Test
    @Transactional
    @DisplayName("dtoToEntity register")
    public void testRegister() {
        // service.register는 ServiceImpl에 구현 + dtoToEntity
        BoardDTO boardDTO = BoardDTO.builder()
                .title("Sample Title")
                .content("Sample Content")
                .writer(clubMemberRepository.getReferenceById("user94@sh.sh"))
                .build();
        System.out.println(service.register(boardDTO));
    }

    // GuestbookService.register는 Imple에 구현 + dtoToEntity기능 테스트
    @Test
    @Transactional
    @DisplayName("페이지 요청 테스트")
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build(); // PageRequestDTO 클래스 참고
        PageResultDTO<BoardDTO, Board> resultDTO = service.getList(pageRequestDTO);

        System.out.println("PREV : " + resultDTO.isPrev());
        System.out.println("NEXT : " + resultDTO.isNext());
        System.out.println("TOTAL : " + resultDTO.getTotalPage());
        //System.out.println((int)(Math.ceil(31/10.0)) * 10);
        System.out.println(resultDTO.toString()); // resultDTO의 내용을 확인가능

        System.out.println("--------entity -> dto 확인------");
        for (BoardDTO boardDTO : resultDTO.getDtoList()) {
            System.out.println(boardDTO);
        }

        System.out.println("=============화면에 출력될 페이지 번호==========");
        resultDTO.getPageList().forEach(i -> System.out.println(i));
    }

    @Test
    @DisplayName("test 서치") //impl, list.html
    public void testSearch(){

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(31)
                .size(10)
                .type("tc")   //검색 조건 t, c, w, tc, tcw ..
                .keyword("title")  // 검색 키워드
                .build();

        PageResultDTO<BoardDTO, Board> resultDTO = service.getList(pageRequestDTO);

        System.out.println("PREV: "+resultDTO.isPrev());
        System.out.println("NEXT: "+resultDTO.isNext());
        System.out.println("TOTAL: " + resultDTO.getTotalPage());

        System.out.println("-------------------------------------");
        for (BoardDTO boardDTO : resultDTO.getDtoList()) {
            System.out.println(boardDTO);
        }

        System.out.println("========================================");
        resultDTO.getPageList().forEach(i -> System.out.println(i));
    }


}
