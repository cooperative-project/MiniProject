package com.codingrecipe.member.lsh.controller;

import com.codingrecipe.member.lsh.dto.BoardDTO;
import com.codingrecipe.member.lsh.dto.CommentDTO;
import com.codingrecipe.member.lsh.service.BoardService;
import com.codingrecipe.member.lsh.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/board/board")
@RequiredArgsConstructor
//@RequestMapping("/board")를 선언해 놓으면
//주소 설정할때 /board/save 이런식으로 안해도됨.
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;
    @GetMapping("save")
    public String saveForm(){
        return "/board/save";
    }
    //@GetMapping 를 두번쓸경우
    // Ambiguous(애매한) mapping. Cannot map 'boardController' method
    //즉 같은 매핑을 두번 쓸 수 없다.

    //@ModelAttribute 말고도 @RequestParam 이용해서 각각 값을 받을 수 도 있다.
    //DTO가 있으면 @ModelAttribute 어노테이션을 이용해 객체를 매개변수로 받을 수 있다.
    //DTO클래스에 있는 필드값이랑 write.html 에 name 값이랑 동일하다면
    // Spring이 해당 필드에 대해 setter 알아서 호출해서 각각 담아준다.
    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException {
        System.out.println("boardDTO = " + boardDTO);
        boardService.save(boardDTO);
        return "index";
    }

    @GetMapping("list")
    public String findAll(Model model){
        //DB에서 현재 게시글 데이터를 가져와서 list.html에 보여준다.
        List<BoardDTO> boardDTOList = boardService.findALL();
        model.addAttribute("boardList",boardDTOList);
        return "/board/list";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model,
                           @PageableDefault(page=1) Pageable pageable){
        /*
            해당 게시판의 조회수를 하나 올리고
            게시글 데이터를 가져와서 detail.html에 출력
         */
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);

        //댓글 목록 가져오기
        List<CommentDTO> commentDTOList = commentService.findAll(id);
        model.addAttribute("commentList",commentDTOList);

        model.addAttribute("board",boardDTO);
        model.addAttribute("page", pageable.getPageNumber());
        return "/board/detail";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model){
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("boardUpdate", boardDTO);
        return "/board/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO, Model model){
        BoardDTO board = boardService.update(boardDTO);
        model.addAttribute("board",board);
        return "/board/detail";
        //return "redirect"/board/" + boardDTO.getId(); ->이러면 수정을 햇는데 조회수가 올라가는 현상 발생
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        boardService.delete(id);
        return "redirect:/";
    }

    //Pageable import는 spring에서 제공하는걸해야함
    // /board/paging?page=1
    @GetMapping("/paging")
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model){
        //pageable.getPageNumber();
        Page<BoardDTO> boardList = boardService.paging(pageable);
        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();

        // page 갯수 20개
        // 현재 사용자가 3페이지
        // 1 2 3
        // 현재 사용자가 7페이지
        // 7 8 9
        // 보여지는 페이지 갯수 3개
        // 총 페이지 갯수 8개

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "/board/paging";

    }

}



//page 갯수 20개
        //현재 사용자가 3페이지
        //1 2 3(색깔이 진함,링크는 없어짐) 4 5
        //현재 사용자가 7페이지
        //7 8 9
        //보여지는 페이지 갯수 3개


