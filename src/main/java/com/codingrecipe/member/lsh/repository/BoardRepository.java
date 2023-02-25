package com.codingrecipe.member.lsh.repository;

import com.codingrecipe.member.lsh.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository  extends JpaRepository<BoardEntity,Long> {
    //update board_table set board_hits=board_hits+1 where id=?
    //entity를 기준으로 작성
    //DB기준으로 할거면 @naveQuery를 사용
    //:id는 @param("id")와 일치해야함
    //Update,Delete실행할 경우 @Modifying 어노테이션 사용
    @Modifying
    @Query(value = "update BoardEntity b set b.boardHits=b.boardHits+1 where b.id=:id")
    void updateHits(@Param("id") Long id);
}
