package com.codingrecipe.member.lsh.service;

import com.codingrecipe.member.lsh.dto.BoardDTO;
import com.codingrecipe.member.lsh.entity.BoardEntity;
import com.codingrecipe.member.lsh.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor //생성자 주입 어노테이션
public class BoardService {
    private final BoardRepository boardRepository;

    //save()메서드는 매개변수를 entity 클래스 타입으로
    //받도록 되어있다. return 도 entity
    //dto->Entity
    public void save(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
        boardRepository.save(boardEntity);
    }


    //findAll은 Repository에서 무엇인가를 가져올때는
    //주로(무조건) Entity로 온다. list형식의
    //entity -> dto변환
    public List<BoardDTO> findALL() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for(BoardEntity boardEntity : boardEntityList){
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));

        }
        return boardDTOList;

    }
    //JPA에서 기본적으로 제공하는 메서드가 아니라 만들어서 쓰는거면
    //@TransActional 어노테이션을 붙여줘야 에러가 안남.
    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if(optionalBoardEntity.isPresent()){
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
        }else{
            return null;
        }
    }

    //save 메서드 id값이 없으면 insert, 있으면 update
    public BoardDTO update(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
        return findById(boardDTO.getId());
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }
}
