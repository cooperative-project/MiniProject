package com.codingrecipe.member.lsh.repository;

import com.codingrecipe.member.lsh.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//JpaRepository<MemberEntity,Long>
//어떤 엔티티를 사용할것인지,pk 데이터 타입 작성 =>공식
public interface MemberRepository extends JpaRepository<MemberEntity, Long>{

    //이메일로 회원 정보 조회(select * from member_table where member_email=?)
    //Optional null 방지 Optional로 감싸서 쓴다 대게
    //하나를 조회할 경우 Optional을 사용
    Optional<MemberEntity> findByMemberEmail(String memberEmail);

}
