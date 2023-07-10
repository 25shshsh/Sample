package com.example.sample.config;

import com.example.sample.security.handler.ClubLoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@Log4j2
public class SecurityConfig {
    // 시큐리티 설정 클래스
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //.antMatchers("/sample/admin").hasRole("ADMIN");
        http
                .csrf().disable() // csrf토큰 비활성화 일반적으로 form태그를 이용하는 방식에서는 권장 , 활성화 시 로그아웃은 post방식
                .authorizeHttpRequests()
        .requestMatchers("/sample/all").permitAll() // 모두 로그인 없이 접근가능 페이지
                .requestMatchers("/sample/member").hasRole("USER") // USER라는 권한을 가져야 접근 가능한 페이지)
                .requestMatchers("/board/register").hasRole("USER")
                .requestMatchers("/board/modify").hasRole("USER")
                .anyRequest().permitAll()// 이외 페이지 모두 허가.
                .and().formLogin()// 인가 / 인증 문제시 로그인 화면을 보여주는 기능 별도의 기능을 지정하려면?
                .and().logout().logoutSuccessUrl("/");

        //http.logout(); // logout페이지 설정 없으면 기본제공페이지, /logout 으로이동하면 뜸

        // 소셜로그인, oauth.properties
       // http.oauth2Login().successHandler(successHandler());

        return http.build();
    }




    @Bean // AuthenticationSuccessHandler의 구현체를 주입받아서 쓰는데 문제 없으려나.
    public ClubLoginSuccessHandler successHandler() {
        return new ClubLoginSuccessHandler();
    }

}
