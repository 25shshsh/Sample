package com.example.sample.board.service;

import com.example.sample.board.dto.BoardDTO;
import com.example.sample.board.entity.Board;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BoardMapper {
    BoardMapper INSTANCE = Mappers.getMapper(BoardMapper.class);

    Board dtoToEntity2(BoardDTO dto);

    BoardDTO entityToDto2(Board entity);
}
