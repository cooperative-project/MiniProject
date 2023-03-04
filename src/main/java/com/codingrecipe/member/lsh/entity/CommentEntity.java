package com.codingrecipe.member.lsh.entity;

import com.codingrecipe.member.lsh.dto.CommentDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "comment_table")
public class CommentEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String commentWriter;

    @Column
    private String commentContents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity boardEntity;


    public static CommentEntity toSaveEntity(CommentDTO commentDTO, BoardEntity boardEntity) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setCommentWriter(commentDTO.getCommentWriter());
        commentEntity.setCommentContents(commentDTO.getCommentContents());
        //넘겨 받은 작성자 내용,게시글,번호만 있는게아니라
        //그 게시글 번호로 조회한 부모 entity값을 넣어줘야한다.
        //참조관계를 맺엇을떄 문법 이라고 생각하면 됨

        //자식 entity를 저장할때는 부모 entity가 필요하다.
        commentEntity.setBoardEntity(boardEntity);
        return commentEntity;
    }
}
