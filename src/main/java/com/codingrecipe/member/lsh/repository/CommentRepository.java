package com.codingrecipe.member.lsh.repository;

import com.codingrecipe.member.lsh.entity.BoardEntity;
import com.codingrecipe.member.lsh.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    //select * from comment_table where board_id=? order by id desc;
    List<CommentEntity> findAllByBoardEntityOrderByIdDesc(BoardEntity boardEntity);
}
