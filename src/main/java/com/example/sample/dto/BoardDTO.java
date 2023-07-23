package com.example.sample.dto;

import com.example.sample.entity.Board;
import com.example.sample.entity.ClubMember;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private Long boardNo;
    private String title;
    private String content;
    private ClubMember writer;
    private LocalDateTime regDate, modDate;

}
