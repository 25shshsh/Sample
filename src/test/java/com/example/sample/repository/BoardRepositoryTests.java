package com.example.sample.repository;

import com.example.sample.board.dto.BoardDTO;
import com.example.sample.board.repository.BoardRepository;
import com.example.sample.member.repository.ClubMemberRepository;
import com.example.sample.board.entity.Board;
import com.example.sample.entity.QBoard;
import com.example.sample.board.service.BoardMapper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.stream.IntStream;

@SpringBootTest
public class BoardRepositoryTests {


    private final BoardRepository boardRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final BoardMapper boardMapper;

    @Autowired
    public BoardRepositoryTests(BoardRepository boardRepository, ClubMemberRepository clubMemberRepository,
                                BoardMapper boardMapper) {
        this.boardRepository = boardRepository;
        this.clubMemberRepository = clubMemberRepository;
        this.boardMapper = boardMapper;
    }

    @Test
    @DisplayName("더미게시글 500개")
    public void insertDummies() {

        IntStream.rangeClosed(1,500).forEach(i -> {

            Board board = Board.builder()
                    .title("Title..." + i)
                    .content("content..." + i)
                    .writer(clubMemberRepository.getReferenceById("user"+(int)(Math.random() * 100) + 1+"@sh.sh"))
                    .build();
            System.out.println(boardRepository.save(board));

        });
    }

    @Test
    public void insertData() {
            Board board = Board.builder()
                    .title("삽입된 게시글")
                    .content("삽입된 내용")
                    .writer(clubMemberRepository.getReferenceById("user88@sh.sh"))
                    .build();
            System.out.println(boardRepository.save(board));
    }

    @Test
    public void updateTest() {
        // 존재하는 번호로 테스트
        Board result = boardRepository.findById(300L)
                .orElseThrow( () -> new IllegalArgumentException("") );

        result.changeTitle("변경된 게시글");
        result.changeContent("변경된 내용");

        boardRepository.save(result);
    }

    @Test
    @DisplayName("단일 항목 검색 테스트")
    public void testQuery1() {
        // springframework Pageable로 불러와라..
        // BooleanExpression은 member.age.eq(xx) 같은 경우처럼 표현식의 결과로 반환되는 값입니다.
        // BooleanBuilder는 이런 표현식을 모아서 사용할 수 있도록 도와주는 도구로 이해하시면 됩니다.
        Pageable pageable = PageRequest.of(0, 10, Sort.by("boardNo").descending());

        QBoard qBoard = QBoard.board; //1 q도메인 클래스 생성

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder(); //2 BooleanBuilder는 where문에 조건절을 넣어주는 컨테이너

        BooleanExpression expression = qBoard.title.contains(keyword); //3 원하는 조건식을 만들어 BooleanExprestion에 삽입

        builder.and(expression); //4 만든 조건을 builder에 and, or같은 키워드로 결합

        Page<Board> result = boardRepository.findAll(builder, pageable); //5 원하는 조건을 + 페이지처리와 동시에!

        // 자료구조가 page인 result를 출력해보자.
        result.stream().forEach(board -> {
            System.out.println(board);
        });
    }

    @Test
    @DisplayName("다중 항목 검색 테스트")
    public void testQuery2() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("boardNo").descending());

        QBoard qBoard = QBoard.board; // q도메인 객체생성

        String keyword = "1";
        BooleanExpression exTitle = qBoard.title.contains(keyword); // 조건식A 생성
        BooleanExpression exContent = qBoard.content.contains(keyword); // 조건식B 생성
        BooleanExpression exAll = exTitle.or(exContent); // 조건식C = A+B

        BooleanBuilder builder = new BooleanBuilder(); // 조건식을 담을 컨테이너 생성

        builder.and(exAll); // Guestbook에 검색 할 (조건1을 무조건 만족하고, 조건2도 무조건 만족하는)
        builder.and(qBoard.boardNo.gt(0L)); // gt, goe, lt ,loe >, >=, <, <=

        Page<Board> result = boardRepository.findAll(builder, pageable);
        result.stream().forEach(board -> {
            System.out.println(board);
        });

    }

    @Test
    @DisplayName("다중 항목 검색 테스트2")
    public void testQuery3() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("boardNo").descending());
        // page위치 조절로 다음 페이지 검색결과도 확인가능.

        QBoard qboard = QBoard.board; // q도메인 객체생성

        String keyword = "145";
        String keyword2 = "9";
        BooleanExpression exTitle = qboard.title.contains(keyword); // 조건식A 생성
        BooleanExpression exContent = qboard.writer.email.contains(keyword2); // 조건식B 생성
        //BooleanExpression exWriter = qGuestbook.writer.contains(keyword2); // 조건식C 생성
        BooleanExpression exAll = exTitle.or(exContent); // A or B
        //BooleanExpression exAll = exTitle.or(exContent.and(exWriter)); // = A or (B and C)

        BooleanBuilder builder = new BooleanBuilder(); // 조건식을 담을 컨테이너 생성

        builder.and(exAll); // Guestbook에 검색 할 (조건 and 조건)
        builder.and(qboard.boardNo.gt(0L)); // gt, lt, goe, loe // >, <, >=, <=

        Page<Board> result = boardRepository.findAll(builder, pageable);
        result.stream().forEach(board -> {
            System.out.println(board);
        });

    }

    @Test
    @Transactional
    @DisplayName("MapStruct 라이브러리 테스트")
    void testMapStruct() {
        Board board = boardRepository.findById(999L)
                .orElseThrow( () -> new IllegalArgumentException("") ); // entity

        System.out.println(board.getRegDate());
        BoardDTO boardDTO = BoardMapper.INSTANCE.entityToDto2(board);

        System.out.println(boardDTO); // dto

        Board board2 = BoardMapper.INSTANCE.dtoToEntity2(boardDTO);

        System.out.println(board2); // entity, 저장전이라 regdate, moddate 정보없음

    }


}
