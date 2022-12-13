package com.its.member.service;

import com.its.member.dto.MemberDTO;
import com.its.member.entity.MemberEntity;
import com.its.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Long save(MemberDTO memberDTO) {
        Long savedId = memberRepository.save(MemberEntity.toSaveEntity(memberDTO)).getId();
        return savedId;

    }

    public MemberDTO login(MemberDTO memberDTO) {
          /*
            email로 DB에서 조회를 하고
            사용자가 입력한 비번과 DB에서 조회한 비번이 일치하는지를 판단해서
            로그인 성공, 실패 여부를 리턴.
            단, email 조회결과가 없을 때도 실패
         */
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if (optionalMemberEntity.isPresent()) {
            MemberEntity memberEntity = optionalMemberEntity.get();
            if (memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
//                MemberDTO memberDTO1 = MemberDTO.toDTO(memberEntity);
//                return memberDTO1;
                return MemberDTO.toDTO(memberEntity);
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    {

    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for(MemberEntity memberEntity: memberEntityList){
            memberDTOList.add(MemberDTO.toDTO(memberEntity));
        }
        return memberDTOList;
    }

    public MemberDTO findById(Long id) {
      Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
      if (optionalMemberEntity.isPresent()){
          return MemberDTO.toDTO(optionalMemberEntity.get());

      }else {
          return null;
      }
    }

    public MemberDTO findByMemberEmail(String loginEmail) {
      Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(loginEmail);
        if (optionalMemberEntity.isPresent()){
            return MemberDTO.toDTO(optionalMemberEntity.get());
        }else {
            return null;
        }
    }

    public void update(MemberDTO memberDTO) {  // 아이디값이 없으면 insert 를 날리고 있으면 update 쿼리를 날림
        MemberEntity updateEntity = MemberEntity.toUpdateEntity(memberDTO);
        memberRepository.save(updateEntity);
    }

    public void delete(Long id){
        memberRepository.deleteById(id);
    }

    public String emailDuplicateCheck(String memberEmail) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(memberEmail);

            if (optionalMemberEntity.isEmpty()){  // 조회했을 떄 비어있으면 사용가능
                return "ok";
            }else {
                return null;
            }
        }




}
//           if (byMemberEmail.isEmpty()){
//               return "ok";
//           }else {
//               return "no";
//           }
//    }

