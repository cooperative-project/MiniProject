package com.codingrecipe.member.lsh.entity;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


//시간 정보를 다룰 수 있는 클래스
//시간 정보를 따로 만든 이유는, 회원 가입할때도 사용하려고
//공통으로 만들어 두었음.
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
    @CreationTimestamp //생성 된 시간
    @Column(updatable = false) //수정시에는 관여안하겠다.
    private LocalDateTime createdTime;

    @UpdateTimestamp//업데이트 된 시간
    @Column(insertable = false) //insert를 할때는 관여를 안하겠다.
    private LocalDateTime updateTime;
}
