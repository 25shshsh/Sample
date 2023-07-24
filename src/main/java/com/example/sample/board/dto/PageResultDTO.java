package com.example.sample.board.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO, EN> {
    // 161p

    // dtoList를 dtolist라고 써서 수정했더니 타임리프 구문 오류가 왜 뜨는지 몰라서 개고생함 오류검출도 안되니까 ㅡㅡ
    private List<DTO> dtoList; // 파라미터를 엔티티로 하는 리스트
    
    private int totalPage; // 총 페이지의 수

    private int page; // 현재 페이지 번호
    private int size; // 페이지 목록의 크기
    
    private int start, end; // 시작하는 페이지 번호, 끝 번호
    
    private boolean prev, next; // 이전, 다음

    private List<Integer> pageList; // 페이지 번호 목록
    
    // jparepository에서 결과를 반환할 때 Page<E> 타입으로 반환 -> 이걸 서비스계층에서 DTO로 변환시켜주고 DTO를 컨트롤러쪽으로 보내야함
    // 다양한 곳에서 사용할 수 있도록 제너릭 타입을 이용하였다. 162p
    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn) {

        // List<DTO> = Page<EN>.stream().map(Function<EN, DTO>).collect(Collectors.toList());
        dtoList = result.stream().map(fn).collect(Collectors.toList());

        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable) {
        this.page = pageable.getPageNumber() + 1; // 0에서 시작하므로 1추가
        this.size = pageable.getPageSize();

        // 현재페이지에서 임시적인 끝번호 ex) 실제 페이지번호가 32개라면 tempEnd는 40 (페이지변경바)
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;
        start = tempEnd - 9;


        // 32와 40중 작은 값이 현재페이지바 내에서 실제End 값
        end = totalPage < tempEnd ? totalPage : tempEnd;

        prev = start > 1;

        // 현재 페이지바에서 임시 끝이 40인데 총페이지갯수가 42개라면 next버튼이 있어야지
        next =  totalPage > tempEnd;

        // boxed() : 원시타입인 IntStream을 List로 담기 위해 Integer클래스로 변환시킨다. , IntStream -> Stream<Integer>
        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}
