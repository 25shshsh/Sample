package com.example.sample.base_entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class }) //Persistence Context에서 객체 변화 감지, GuestbookApplication 수정도 필요
@Getter
public abstract class BaseEntity {
    // 변경되는 Entity 값으로 regDate와 modData에 적절한 값이 지정된다.

    @CreatedDate // Entity의 생성 시간을 처리
    @Column(name = "regdate", updatable = false) // updatable 비활성화로 DB에 반영x, 등록시간처럼 사용가능
    private LocalDateTime regDate;

    @LastModifiedDate // 최종 수정 시간을 자동으로 처리하는 용도로 사용
    @Column(name="moddate")
    private LocalDateTime modDate;
}
