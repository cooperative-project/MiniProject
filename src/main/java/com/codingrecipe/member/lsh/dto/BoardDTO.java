package com.codingrecipe.member.lsh.dto;

import com.codingrecipe.member.lsh.entity.BoardEntity;
import com.codingrecipe.member.lsh.entity.BoardFileEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private List<MultipartFile> boardFile; //save.html --> Controller 파일 담는 용도
    private List<String> orginalFileName; //원본 파일 이름
    private List<String> storedFileName; //서버 저장용 파일 이름
    private int fileAttached; //파일 첨부 여부(첨부 1, 미첨부 0)


    //생성자 단축키 alt+insert 아니면 오른쪽 마우스클릭 generate


    public BoardDTO(Long id, String boardWriter, String boardTitle, int boardHits, LocalDateTime boardCreatedTime) {
        this.id = id;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;
    }

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
        if(boardEntity.getFileAttached() == 0){
            boardDTO.setFileAttached(boardEntity.getFileAttached());
        }else{
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();
            boardDTO.setFileAttached(boardEntity.getFileAttached());
            for(BoardFileEntity boardFileEntity : boardEntity.getBoardFileEntityList()){
                originalFileNameList.add(boardFileEntity.getOriginalFileName());
                storedFileNameList.add(boardFileEntity.getStoredFileName());
            }
            boardDTO.setOrginalFileName(originalFileNameList);
            boardDTO.setStoredFileName(storedFileNameList);


            //파일 이름을 가져가야함
            //orginalFileName, storedFileName : board_file_table(BoardFileENtity)
            //join
            //select * from board_table b, board_file_table bf where b.id = bf.board_id
            //and where b.id = ?
            //.get(0) 첨부파일 1개일때
            //boardDTO.setOrginalFileName(boardEntity.getBoardFileEntityList().get(0).getOriginalFileName());
            //boardDTO.setStoredFileName(boardEntity.getBoardFileEntityList().get(0).getStoredFileName());
        }

        return boardDTO;

    }
}
