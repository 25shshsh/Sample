package com.example.sample.member.service;

import com.example.sample.member.entity.ClubMember;
import com.example.sample.member.entity.ClubMemberRole;
import com.example.sample.member.repository.ClubMemberRepository;
import com.example.sample.member.dto.ClubAuthMemberDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ClubOAuth2UserDetailsService extends DefaultOAuth2UserService { // DefaultOAuth2UserService
    // 외부API로부터의 인증결과 정보를 받아왔을때 그것을 처리하는 역할

    private final ClubMemberRepository clubMemberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ClubOAuth2UserDetailsService(ClubMemberRepository clubMemberRepository,
                                        PasswordEncoder passwordEncoder) {
        this.clubMemberRepository = clubMemberRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // 이미 구현된 loadUser메서드를 가져다가 그냥 쓴다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("--------------------------------------");
        log.info("userRequest : " + userRequest);

        String clientName = userRequest.getClientRegistration().getClientName(); // 113? sub값

        log.info("clientName : " + clientName); // Google
        log.info(userRequest.getAdditionalParameters()); // {id_token=~~~~~}

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("====================================="); // 외부API Attribute는 Map<String, Object>구조로 되어있다.
        oAuth2User.getAttributes().forEach( (k,v) -> {
            log.info(k + ":" + v); // 오 MAP형태에서 각 API동의한 Attribute들이 나오네(sub, picture, email, email_verified)
        });

        // 각 요소를 확인했으니 DB에 담아주기 위한 작업
        String email = null;

        if(clientName.equals("Google")) { // 구글을 이용하는 경우
            email = oAuth2User.getAttribute("email"); // 이메일을 추출
        }
        log.info("EMAIL : " + email);
/*        ClubMember member = saveSocialMember(email);

        return oAuth2User;*/

        // 소셜로그인 한 정보를 ClubMember에 담고 그걸 DTO에 담고 DTO를 리턴한다.
        ClubMember member = saveSocialMember(email);

        ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
                member.getEmail(),
                member.getPassword(),
                true,
                member.getRoleSet().stream().map(
                        role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toList()),
                oAuth2User.getAttributes() // saveSocialMember()에서 만든 객체에 attr추가 후 -> DTO틀에 담아줌
        );

        clubAuthMember.setName(member.getName());

        return clubAuthMember;
        // 외부에서 받아오는 값을 뽑아서 DB에 저장 후 DTO에 맞춰서 변경시킨 것

    }

    //socialMember의 email을 담으면 저장 후 clubmember로 리턴.
    private ClubMember saveSocialMember (String email) {

        Optional<ClubMember> result = clubMemberRepository.findByEmail(email, true);

        // 기존에 동일한 아이디(email)가 있다면? 조회만
        if(result.isPresent()) {
            return result.get();
        }

        // 없다면 회원가입하고 리턴
        ClubMember clubMember = ClubMember.builder()
                .email(email)
                .name(email)
                .password(passwordEncoder.encode("1111"))
                .fromSocial(true)
                .build();
        clubMember.addMemberRole(ClubMemberRole.USER);
        clubMemberRepository.save(clubMember);

        return clubMember;
    }

}
