package com.example.sample.board.service;

import com.example.sample.board.dto.BoardDTO;
import com.example.sample.board.dto.PageRequestDTO;
import com.example.sample.board.dto.PageResultDTO;
import com.example.sample.board.entity.Board;
import com.example.sample.board.entity.QBoard;
import com.example.sample.board.repository.BoardRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;


@Service
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository; // final 붙이면 생성될 때 한번만 호출된다. 호출되는 객체의 수정을 방지한다.

    @Override
    public Long register(BoardDTO dto) {

        log.info("DTO-------------------");
        log.info(dto);


        log.info("dtoToEntity-----------");
        Board entity = dtoToEntity(dto);
        log.info(entity);

        boardRepository.save(entity);


        return entity.getBoardNo();
    }

    @Override
    public BoardDTO read(Long boardNo) {
        Optional<Board> result = boardRepository.findById(boardNo);

        return result.isPresent()? entityToDto(result.get()): null;
    }

    @Override
    public void remove(Long boardNo) {
        boardRepository.deleteById(boardNo);
    }

    @Override
    public void modify(BoardDTO dto) { // 업데이트하는 항목은 제목, 내용
        Optional<Board> result = boardRepository.findById(dto.getBoardNo());

        if(result.isPresent()){
            Board entity = result.get();

            // 게시글 수정 : entity를 꺼내서 수정된 dto의 값을 넣어주고 저장
            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());

            boardRepository.save(entity);
        }
    }

    @Override // 164p
    public PageResultDTO<BoardDTO, Board> getList(PageRequestDTO requestDTO) {

        Pageable pageable = requestDTO.getPageable(Sort.by("boardNo").descending());

        BooleanBuilder booleanBuilder = buildSearchCondition(requestDTO); // 검색조건을 어떻게 처리할 것인지

        Page<Board> result = boardRepository.findAll(booleanBuilder, pageable); // Querydsl 사용

        Function<Board, BoardDTO> fn = (entity -> entityToDto(entity)); // static class에 Function정리함.

        return new PageResultDTO<>(result, fn);
    }

    private BooleanBuilder buildSearchCondition(PageRequestDTO requestDTO) {

        String type = requestDTO.getType();

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QBoard qBoard = QBoard.board;

        String keyword = requestDTO.getKeyword();

        BooleanExpression expression = qBoard.boardNo.gt(0L); // boardNo > 0 조건만 생성

        booleanBuilder.and(expression);

        if(type == null || type.trim().length() == 0){ //검색 조건이 없는 경우
            return booleanBuilder;
        }

        // 검색 조건을 작성하기
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(type.contains("t")){
            conditionBuilder.or(qBoard.title.contains(keyword));
        }
        if(type.contains("c")){
            conditionBuilder.or(qBoard.content.contains(keyword));
        }
        if(type.contains("w")){
            conditionBuilder.or(qBoard.writer.email.contains(keyword));
        }
        // 만약 list.html에서 tcw라면 제목,내용,작성자 모두 포함이지


        //모든 조건 통합
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    }
}
