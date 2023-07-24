package com.example.sample.member.service;

import com.example.sample.member.entity.ClubMember;
import com.example.sample.member.repository.ClubMemberRepository;
import com.example.sample.member.dto.ClubAuthMemberDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Log4j2
@Service // 자동으로 스프링에서 빈으로 처리
public class ClubUserDetailsService implements UserDetailsService {
    // 사용자의 정보를 가져오는 핵심역할 , '인가' 로 내부 인증 결과를 처리하는 역할, 얘 때문에 dto로 변환시켜야 했다.

    private final ClubMemberRepository clubMemberRepository;

    @Autowired
    public ClubUserDetailsService (ClubMemberRepository clubMemberRepository) {
        this.clubMemberRepository = clubMemberRepository;
    }



    @Override // username이란 /login에서 입력받는 아이디 값.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("ClubUserDetailsService loadUserByUsername 입력받은 아이디 " + username);

        ClubMember clubMember = clubMemberRepository.findByEmail(username, false)
                .orElseThrow( () -> new UsernameNotFoundException("Check Email or Social") );

        log.info("--------------------------------");
        log.info("입력받은 아이디를 DB에서 확인 " + clubMember);

/*        Optional<ClubMember> result = clubMemberRepository.findByEmail(username, false);

        if (!result.isPresent()) {
            throw new UsernameNotFoundException("Check Email or Social");
        }

        ClubMember clubMember = result.get();
        log.info("-----------------------------------------------");
        log.info(clubMember);*/

        ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
                clubMember.getEmail(), // this.email = username
                clubMember.getPassword(), // 왜 null이 될까요.
                clubMember.isFromSocial(),
                clubMember.getRoleSet().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toSet()) // ?
        );

        clubAuthMember.setName(clubMember.getName());
        clubAuthMember.setFromSocial(clubMember.isFromSocial());
        clubAuthMember.setPassword(clubMember.getPassword()); // 위에서 null이라서 따로 지정해줬습니다.
        log.info("입력받은 아이디로 DTO  " + clubAuthMember);

        return clubAuthMember;
    }
}
