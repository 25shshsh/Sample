package com.example.sample.security;

import com.example.sample.member.entity.ClubMember;
import com.example.sample.member.entity.ClubMemberRole;
import com.example.sample.member.repository.ClubMemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ClubMemberTests {

    private final ClubMemberRepository clubMemberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ClubMemberTests (ClubMemberRepository clubMemberRepository,
                            PasswordEncoder passwordEncoder) {
        this.clubMemberRepository = clubMemberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Test
    @DisplayName("회원가입 100개")
    void insertDummies() {
        // 1~80까지는 USER,
        // 81-90까지는 USER, MANAGER
        // 91-100까지는 USER, MANAGER, ADMIN 조건 걸면서 중복추가!

        IntStream.rangeClosed(1,100).forEach( i -> {
            ClubMember clubMember = ClubMember.builder()
                    .email("user" + i + "@sh.sh")
                    .name("sh" + i)
                    .fromSocial(false)
                    .password(passwordEncoder.encode("1111") )
                    .build();

            // default role : 아~
            clubMember.addMemberRole(ClubMemberRole.USER);

            if (i > 80) {
                clubMember.addMemberRole(ClubMemberRole.MANAGER);
            }
            if (i > 90) {
                clubMember.addMemberRole(ClubMemberRole.ADMIN); // 이것 (까지) 추가임.
            }
            clubMemberRepository.save(clubMember);
        });
    }

    @Test
    @DisplayName("특정멤버 구성요소 확인")
    void testRead() {
        Optional<ClubMember> result = clubMemberRepository.findByEmail("user94@sh.sh", false);

        ClubMember clubMember = result.get();
        System.out.println("clubMember = " + clubMember);
        System.out.println("clubMember.getRoleSet() = " + clubMember.getRoleSet()); // [ADMIN, MANAGER, USER]

        // 암호화된 1111과 1111이 일치하는지.
        boolean matchResult = passwordEncoder.matches("1111", clubMember.getPassword());
        System.out.println("matchResult = " + matchResult);
    }
}
