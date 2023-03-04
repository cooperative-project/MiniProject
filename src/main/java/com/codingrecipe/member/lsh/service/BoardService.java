package com.codingrecipe.member.lsh.service;

import com.codingrecipe.member.lsh.dto.BoardDTO;
import com.codingrecipe.member.lsh.entity.BoardEntity;
import com.codingrecipe.member.lsh.entity.BoardFileEntity;
import com.codingrecipe.member.lsh.repository.BoardFileRepository;
import com.codingrecipe.member.lsh.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor //생성자 주입 어노테이션
public class BoardService {
    private final BoardRepository boardRepository;

    private final BoardFileRepository boardFileRepository;

    //save()메서드는 매개변수를 entity 클래스 타입으로
    //받도록 되어있다. return 도 entity
    //dto->Entity
    public void save(BoardDTO boardDTO) throws IOException {
        //파일 첨부 여부에 따라 로직 분리
        if(boardDTO.getBoardFile().isEmpty()){
            //첨부 파일 없음
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
            boardRepository.save(boardEntity);
        }else {
            //첨부 파일 있음
            /*
                1.DTO에 담긴 파일을 꺼냄
                2.파일의 이름 가져옴
                3.서버 저장용 이름을 만듦
                //내자신.jpg -->38128318183_내사진.jpg
                4.저장 경로 설정
                5.해당 경로에 파일 저장
                6.board_table에 해당 데이터 save 처리
                7.board_file_table에 해당 데이터 save 처리

            */
            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
            //optional 생략
            //부모 entity자체를 자식 entity에다 저장하기 위해선 부모 entity 자체가 전달되어야 한다.
            Long saveId = boardRepository.save(boardEntity).getId();
            BoardEntity board = boardRepository.findById(saveId).get();
            for(MultipartFile boardFile: boardDTO.getBoardFile()){
                //밑에 코드는 DTO에 담겨져있는 파일을 꺼내는 용도
                //하지만 다중파일을 꺼낼때는 for문을 사용하므로 사용할 필요가 없음 밑에 코드는
                //MultipartFile boardFile = boardDTO.getBoardFile();

                String originalFilename = boardFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + "_" + originalFilename;  //3번내용
                String savePath = "C:\\springboot_img\\" + storedFileName; // C:/springboot_img/1235959393_내사진.jpg
                boardFile.transferTo(new File(savePath));//파일을 넘긴다 5번

                //db테이블에 저장해야 하기 때문에 entity로 변환
                //boardFileEntity 로 변환하는 작업
                BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board,originalFilename, storedFileName);
                boardFileRepository.save(boardFileEntity);
            }




        }

    }


    //findAll은 Repository에서 무엇인가를 가져올때는
    //주로(무조건) Entity로 온다. list형식의
    //entity -> dto변환
    @Transactional
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

    //부모 entity 에서 자식 entity 에 접근할경우
    //내용 호출하는 함수 에다 어노테이션붙여야함
    @Transactional
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

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() -1;
        int pageLimit = 3; //한 페이지에 보여줄 글 갯수
        //한 페이지당 3개씩 글을 보여주고 정렬 기준은 entity 에 pk 인 id 기준으로 내림차순 정렬
        //page 위치에 있는 값은 0부터 시작 그래서 -1을 함
        Page<BoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC,"id"))); //properties는 entity 작성한 pk기준

        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부

        //list 객체로 담아가면 위에 메서드 사용못함
        //board는 entity 객체
        //목록: id, writer, title, hits, createdTime
        //.map(entity->dto))
        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getId(),board.getBoardWriter(),board.getBoardTitle(),board.getBoardHits(),board.getCreatedTime()));
        return boardDTOS;
    }
}
