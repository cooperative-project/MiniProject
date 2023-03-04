package com.codingrecipe.member.lsh.dto;

import com.codingrecipe.member.lsh.entity.CommentEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentDTO {
    private Long id;
    private String commentWriter;
    private String commentContents;
    private Long boardId;
    private LocalDateTime commentCreatedTime;

    public static CommentDTO toCommentDTO(CommentEntity commentEntity,Long boardId) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setCommentWriter(commentEntity.getCommentWriter());
        commentDTO.setCommentContents(commentEntity.getCommentContents());
        commentDTO.setCommentCreatedTime(commentEntity.getCreatedTime());
        
        //자식 entity에 있는 부모 entity의 id를 호출(해당 메서드 매개변수 commentEntity만 있을경우
        //또한 service메서드에 @Transactional붙여야 한다.
        //commentDTO.setBoardId(commentEntity.getBoardEntity().getId());
        commentDTO.setBoardId(boardId);
        return commentDTO;
    }
}
