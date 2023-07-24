package com.example.sample.board.entity;

import com.example.sample.base_entity.BaseEntity;
import com.example.sample.member.entity.ClubMember;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "writer")
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardNo;

    @Column(length = 100, nullable = false)
    private String title;


    @Column(length = 1500, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClubMember writer;

    @Override
    public LocalDateTime getModDate() {
        return super.getModDate();
    }

    @Override
    public LocalDateTime getRegDate() {
        return super.getRegDate();
    }

    public void changeTitle(String title) {
        this.title=title;
    }

    public void changeContent(String content) {
        this.content = content;
    }
    // 제목과 내용의 수정이 끝나고 sava할 때 modtime도 변경

}
