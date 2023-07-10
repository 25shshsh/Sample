package com.example.sample.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordTests {


    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordTests(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Test
    public void testEncode() {

        String password = "1111";
        String enPw = passwordEncoder.encode(password);
        System.out.println("enPw : " + enPw); // 인코딩 된 패스워드 해시 값

        // 1111을 암호화 한 결과와 1111에 맞는지 boolean으로 확인
        boolean matchResult = passwordEncoder.matches(password, enPw); // (rowPassword, encodingPassword)
        System.out.println("matchResult : " + matchResult);
    }
}
