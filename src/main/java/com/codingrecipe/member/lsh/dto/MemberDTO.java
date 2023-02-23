package com.codingrecipe.member.lsh.dto;

import com.codingrecipe.member.lsh.entity.MemberEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDTO {
    private long id;
    private String memberEmail;
    private String memberPassword;
    private String memberName;

    //entity -> dto
    public static MemberDTO toMemberDTO(MemberEntity memberEntity){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(memberEntity.getId());
        memberDTO.setMemberName(memberEntity.getMemberName());
        memberDTO.setMemberEmail(memberEntity.getMemberEmail());
        memberDTO.setMemberPassword(memberEntity.getMemberPassword());

        return memberDTO;

    }
}
