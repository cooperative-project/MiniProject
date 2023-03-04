package com.codingrecipe.member.lsh.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="board_file_table")
public class BoardFileEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String originalFileName;
    @Column
    private String storedFileName;

    //EAGER은 부모 entity를 조회할때 자식 entity 무조건 가져옴
    //LAZY는 필요한 상황에서만 호출함. 주로 사용!
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id") //실제 db에 만들어지는 컬럼이름
    private BoardEntity boardEntity;//타입은 Long 이아닌 부모 entity로 주어야한다.
    //실제 db에는 int, bigint 처럼 들어가있다.

    //자식 entity를 저장할때는 부모 entity가 필요하다.
    public static BoardFileEntity toBoardFileEntity(BoardEntity boardEntity, String originalFileName, String storedFileName){
        BoardFileEntity boardFileEntity = new BoardFileEntity();
        boardFileEntity.setOriginalFileName(originalFileName);
        boardFileEntity.setStoredFileName(storedFileName);
        //pk값이 아닌 부모 entity 자체를 보내줘야 한다.
        boardFileEntity.setBoardEntity(boardEntity); //pk값이 아니라 부모 entity 값을 전달
        return boardFileEntity;
    }

}
