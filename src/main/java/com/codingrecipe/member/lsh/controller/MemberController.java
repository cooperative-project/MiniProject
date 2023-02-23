package com.codingrecipe.member.lsh.controller;

import com.codingrecipe.member.lsh.dto.MemberDTO;
import com.codingrecipe.member.lsh.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    //생성자 주입
    private final MemberService memberService;

    //회원가입 페이지 출력 요청
    @GetMapping("/member/save")
    public String saveForm() {
        return "save";
    }

    @PostMapping("/member/save")
    public String save(@ModelAttribute MemberDTO memberDTO){
        System.out.println("MemberController.save");
        System.out.println("memberDTO = " + memberDTO);
        memberService.save(memberDTO);
        return "login";
    }

    //controller 에서 login페이지를 띄워주는 역할을 함.
    @GetMapping("/member/login")
    public String loginForm(){
        return "login";
    }
    @PostMapping("/member/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session){
        MemberDTO loginResult = memberService.login(memberDTO);
        if(loginResult !=null){
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            return "main";
        }else{
            return "login";
        }
    }
    
    @GetMapping("/member/")
    //findAll ->DB내용을 전부다 끌고온다.
    //회원은 여러개 이므로 ArrayList타입을 사용
    //다양한 데이터일경우 Map, 아니면 List
    
    //model객체에서 list를 담을거임
    public String findAll(Model model){
        List<MemberDTO> memberDTOList = memberService.findAll();
        //어떠한 html로 가져갈 데이터가 있다면 model 사용
        model.addAttribute("memberList", memberDTOList);
        return "list";
    }

    @GetMapping("/member/{id}")
    public String findById(@PathVariable Long id, Model model){
        //여러명 받을 거면 findAll, 여기서는 한명만 받을 것 이기때문에 MemberDTO로 받음
        MemberDTO memberDTO = memberService.findById(id);

        //Model은 HTML로 넘어간다.
        model.addAttribute("member", memberDTO);
        return "detail";
    }

    @GetMapping("/member/update")
    public String updateForm(HttpSession session, Model model){
        //getAttribute는 return 값이 Object
        //String 으로 형변환 해야 오류가 안남.
        //보낼땐 set 가져올땐 get
        String myEmail = (String)session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.updateForm(myEmail);
        model.addAttribute("updateMember",memberDTO);
        return "update";
    }

    @PostMapping("/member/update")
    public String update(@ModelAttribute MemberDTO memberDTO){
        memberService.update(memberDTO);
        return "redirect:/member/" + memberDTO.getId();
    }
}
