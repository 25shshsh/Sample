package com.example.sample.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;


import java.util.Collection;
import java.util.Map;

@Log4j2
@Getter
@Setter
@ToString
public class ClubAuthMemberDTO extends User implements OAuth2User {

    // DTO의 역할을 수행하는 동시에, 스프링 시큐리티에서 인가/인증 작업에 사용할 수 있다.
    private String email;
    private String password;
    private String name;
    private boolean fromSocial;
    private Map<String, Object> attr;


    public ClubAuthMemberDTO(
            String username,
            String password,
            boolean fromSocial,
            Collection<? extends GrantedAuthority> authorities) {

        super(username, password, authorities);
        this.email = username;
        this.fromSocial = fromSocial;
    }

    // 받아오는 OAuth2User(인터페이스를 상속)를 saveSocialMember()한 결과로 나오는 ClubMember를 이용하여 dto를 구성한다.
    // attr안에 모든 정보가 담겨서 온다는 차이점이 있음.
    public ClubAuthMemberDTO(
            String username,
            String password,
            boolean fromSocial,
            Collection<? extends GrantedAuthority> authorities,
            Map<String, Object> attr) {

        this(username, password,fromSocial, authorities);
        this.attr = attr;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attr;
    }
}
