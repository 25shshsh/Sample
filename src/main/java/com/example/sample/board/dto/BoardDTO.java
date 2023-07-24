package com.example.sample.board.dto;

import com.example.sample.member.entity.ClubMember;
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
