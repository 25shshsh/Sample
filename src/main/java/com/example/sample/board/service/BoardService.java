package com.example.sample.board.service;

import com.example.sample.board.dto.BoardDTO;
import com.example.sample.board.dto.PageRequestDTO;
import com.example.sample.board.dto.PageResultDTO;
import com.example.sample.board.entity.Board;


public interface BoardService {


    Long register(BoardDTO dto);

    PageResultDTO<BoardDTO, Board> getList(PageRequestDTO requestDTO);

    BoardDTO read(Long boardNo);

    void remove(Long boardNo);

    void modify(BoardDTO dto);

    // java8부터 default를 이용해 인터페이스 > 추상 > 구현의 형식에서 추상클래스 생략 가능.
    // 인터페이스에서 바로 구현클래스에서 추가할 필요없이 바로 동작할 수 있는 dtoToEntitiy 메서드 구성
/*    default Board dtoToEntity(BoardDTO dto) {
        Board entity = Board.builder()
                .boardNo(dto.getBoardNo())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    }
    // default 리턴타입 메서드명(파라미터){};
    default BoardDTO entityToDto(Board entity) {
        BoardDTO dto = BoardDTO.builder()
                .boardNo(entity.getBoardNo())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
        return dto;
    }*/

}
