package com.example.sample.dto;

import com.example.sample.entity.ClubMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data // getter,setter 자유롭게 값을 변경가능.
public class BoardDTO {
    private Long boardNo;
    private String title;
    private String content;
    private ClubMember writer;
    private LocalDateTime regDate, modDate;
}
