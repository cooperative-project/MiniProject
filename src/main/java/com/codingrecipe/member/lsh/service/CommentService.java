package com.codingrecipe.member.lsh.service;

import com.codingrecipe.member.lsh.dto.CommentDTO;
import com.codingrecipe.member.lsh.entity.BoardEntity;
import com.codingrecipe.member.lsh.entity.CommentEntity;
import com.codingrecipe.member.lsh.repository.BoardRepository;
import com.codingrecipe.member.lsh.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    //@RequiredArgsConstructor 생성자 주입 어노테이션이 없으면..
    //@Autowired
    //public CommentService(CommentRepository commentRepository, BoardRepository boardRepository)
    //{
    //this.commentRepository = commentRepository
    //this.boardRepository = boardRepository
    //}   --> 이렇게 생성자를 생성해야 한다.

    public Long save(CommentDTO commentDTO) {
        //CommentEntity commentEntity = new CommentEntity();
        //원래는 위에 코드 작성해야하는데 entity는 철저하게 보호해야 한다
        //라는 것 때문에 굳이 선언하지 않는다.
        //builder쓰기도한다.

        //부모 entity 조회
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO, boardEntity);
            return commentRepository.save(commentEntity).getId();
        } else {
            return null;
        }
    }

    /*
    댓글 달기전에 부모게시판 아이디를 알아야 하기때문에
    commentRepository에서 만든 메서드 사용
    findById -> Optional 을 바로 사용
    댓글 목록들을 Entity 하나하나 반복문을 이용해서 DTO로
    옮겨 담는다.
     */
    public List<CommentDTO> findAll(Long boardId) {
        //select * from comment_table where board_id=? order by id desc;
        //findById(boardId) -> Optional<>~~~ .ispresent 까지 한번에 해결
        BoardEntity boardEntity = boardRepository.findById(boardId).get();
        List<CommentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByIdDesc(boardEntity);
        /* EntityList -> DTOList */
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (CommentEntity commentEntity: commentEntityList) {
            CommentDTO commentDTO = CommentDTO.toCommentDTO(commentEntity, boardId);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }
}
