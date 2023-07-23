package com.example.sample.service;

import com.example.sample.dto.BoardDTO;
import com.example.sample.entity.Board;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BoardMapper {
    BoardMapper INSTANCE = Mappers.getMapper(BoardMapper.class);

    Board dtoToEntity2(BoardDTO dto);

    BoardDTO entityToDto2(Board entity);
}
