package com.example.sample.board.service;

import com.example.sample.board.dto.BoardDTO;
import com.example.sample.board.entity.Board;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-16T02:55:27+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class BoardMapperImpl implements BoardMapper {

    @Override
    public Board dtoToEntity(BoardDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Board.BoardBuilder board = Board.builder();

        board.boardNo( dto.getBoardNo() );
        board.title( dto.getTitle() );
        board.content( dto.getContent() );
        board.writer( dto.getWriter() );

        return board.build();
    }

    @Override
    public BoardDTO entityToDto(Board entity) {
        if ( entity == null ) {
            return null;
        }

        BoardDTO.BoardDTOBuilder boardDTO = BoardDTO.builder();

        boardDTO.boardNo( entity.getBoardNo() );
        boardDTO.title( entity.getTitle() );
        boardDTO.content( entity.getContent() );
        boardDTO.writer( entity.getWriter() );
        boardDTO.regDate( entity.getRegDate() );
        boardDTO.modDate( entity.getModDate() );

        return boardDTO.build();
    }
}
