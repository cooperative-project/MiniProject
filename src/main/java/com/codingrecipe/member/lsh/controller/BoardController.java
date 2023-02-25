package com.codingrecipe.member.lsh.controller;

import com.codingrecipe.member.lsh.dto.BoardDTO;
import com.codingrecipe.member.lsh.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
//@RequestMapping("/board")를 선언해 놓으면
//주소 설정할때 /board/save 이런식으로 안해도됨.
public class BoardController {
    private final BoardService boardService;
    @GetMapping("/board/write")
    public String writeForm(){
        return "/board/write";
    }
    //@GetMapping 를 두번쓸경우
    // Ambiguous(애매한) mapping. Cannot map 'boardController' method
    //즉 같은 매핑을 두번 쓸 수 없다.

    //@ModelAttribute 말고도 @RequestParam 이용해서 각각 값을 받을 수 도 있다.
    //DTO가 있으면 @ModelAttribute 어노테이션을 이용해 객체를 매개변수로 받을 수 있다.
    //DTO클래스에 있는 필드값이랑 write.html 에 name 값이랑 동일하다면
    // Spring이 해당 필드에 대해 setter 알아서 호출해서 각각 담아준다.
    @PostMapping("/board/write")
    public String write(@ModelAttribute BoardDTO boardDTO){
        System.out.println("boardDTO = " + boardDTO);
        boardService.save(boardDTO);
        return "index";
    }

    @GetMapping("/board/list")
    public String findAll(Model model){
        //DB에서 현재 게시글 데이터를 가져와서 list.html에 보여준다.
        List<BoardDTO> boardDTOList = boardService.findALL();
        model.addAttribute("boardList",boardDTOList);
        return "/board/list";
    }

    @GetMapping("/board/{id}")
    public String findById(@PathVariable Long id, Model model){
        /*
            해당 게시판의 조회수를 하나 올리고
            게시글 데이터를 가져와서 detail.html에 출력
         */
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board",boardDTO);
        return "/board/detail";
    }
}
