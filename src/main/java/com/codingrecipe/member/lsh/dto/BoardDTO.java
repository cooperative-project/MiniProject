package com.codingrecipe.member.lsh.dto;

import com.codingrecipe.member.lsh.entity.BoardEntity;
import lombok.*;

import java.time.LocalDateTime;

//DTO(Data Transfer Object) 데이터를 전송할때 쓰는 객체
//VO, Bean 같은느낌,비슷한 용어(목적이 같음)
//Entity랑은 다름
//데이터를 주고 받을 때 파라미터들이 여러개 있다.
//이것을 따로따로 보내기에는 관리하기가 쉽지않다.
//그래서 하나의 객체에 담아서 관리하자 해서 쓰이게됨.
@Getter
@Setter
@ToString
@NoArgsConstructor //기본생성자
@AllArgsConstructor //모든 필드를 매개변수로 하는 생성자
public class BoardDTO {
    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private int boardHits;
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdateTime;


    public static BoardDTO toBoardDTO(BoardEntity boardEntity){
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardPass(boardEntity.getBoardPass());
        boardDTO.setBoardCreatedTime(boardEntity.getCreatedTime());
        boardDTO.setBoardUpdateTime(boardEntity.getUpdateTime());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        return boardDTO;

    }
}
