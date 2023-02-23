package com.codingrecipe.member.lsh.service;

import com.codingrecipe.member.lsh.dto.MemberDTO;
import com.codingrecipe.member.lsh.entity.MemberEntity;
import com.codingrecipe.member.lsh.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    public void save(MemberDTO memberDTO){

        //1. dto -> entity 변환
        //2. repository의 save 메서드 호출(조건, entity객체를 넘겨줘야함)
        //jpa에서는 자동으로 save메서드를 지원한다.
        //alt+enter단축키
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);

        //repository의 save메서드 호출(조건, entity객체를 넘겨줘야 함
    }
    
    //바로 DB로 쏘는게아니라 Controller에서 이 메서드를 사용할 것임
    public MemberDTO login(MemberDTO memberDTO){
        //1.회원이 입력한 이메일로 DB에서 조회를 함
        //2.DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단

        //entity 객체를 Optional로 감싼느낌, 즉 포장이 2번되어있다.
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail()); 
        if(byMemberEmail.isPresent()){
            //조회 결과가 있다.(해당 이메일을 가진 회원 정보가 있다)
            //.get 안에 있는 객체를 가져옴 entity객체를
            
            //Optional로 감싸진 객체를 한번 벗겨낸다. .get()
            MemberEntity memberEntity = byMemberEmail.get();
            if(memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())){
                //비밀번호가 일치
                //entity -> dto 변환후 리턴

                //entity 객체를 어디까지 끌고올 것인가 이것은 개인취향에 따라 다름
                //해당 프로젝트는 service에는 entity객체사용 Controller에서는 DTO객체사용
                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                return dto;
            }else {
                //비밀번호 불일치(로그인 실패)
                return null;
            }
        }else{
            //조회 결과가 없다(해당 이메일을 가진 회원이 없다)
            return null;
        }
    
    }

    //entity -> dto 해서 controller한테 줘야함
    //Repository 와 관련된 것은 무조건 Entity
    //entity 가 여러개 담긴걸 dto로 옮겨야함
    //entity 하나하나꺼내서 dto로 옮겨야함. 즉 for 반복문 사용
    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for(MemberEntity memberEntity:memberEntityList){
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
            /*
            MemberDTO memberDTO = MemberDTO.toMemberDTO(memberEntity);
            memberDTOList.add(memberDTO);
            */
        }
        return memberDTOList;
    }

    //Optional이라는 포장지를 벗겨낸다. .isPresnet, .get을 이용
    //Entity를 DTO에 담는다.
    //dto로 리턴후 Controller로 보낸다.
    public MemberDTO findById(Long id) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if(optionalMemberEntity.isPresent())
        {
            /*
            MemberEntity memberEntity = optionalMemberEntity.get();
            MemberDTO memberDTO = MemberDTO.toMemberDTO(memberEntity);
            return memberDTO; 랑 동일 아래 1문장이랑
             */
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        }else{
            return null;
        }

    }

    //entity->dto

    public MemberDTO updateForm(String myEmail) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(myEmail);
        if(optionalMemberEntity.isPresent()){
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        }else{
            return null;
        }
    }

    public void update(MemberDTO memberDTO) {
        memberRepository.save(MemberEntity.toUpdateMemberEntity(memberDTO));
    }
}
