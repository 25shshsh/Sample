package com.example.sample.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {
    //목록 페이지를 요청할 때 사용하는 데이터를 재사용하기 쉽게 만드는 클래스.

    private int page;
    private int size;

    private String type;
    private String keyword;

    // 페이지 기본값.
    public PageRequestDTO() {
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageable(Sort sort) {
        return PageRequest.of(page - 1, size, sort.descending()); // Pageable 리턴
    }






}
