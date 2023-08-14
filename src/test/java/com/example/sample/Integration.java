package com.example.sample;

import com.example.sample.board.dto.BoardDTO;
import com.example.sample.board.entity.Board;
import com.example.sample.board.service.BoardMapper;
import com.example.sample.member.entity.ClubMember;
import com.example.sample.member.entity.ClubMemberRole;
import com.example.sample.member.repository.ClubMemberRepository;
import com.example.sample.board.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.IntStream;

@SpringBootTest

public class Integration {

    private final ClubMemberRepository clubMemberRepository;
    private final PasswordEncoder passwordEncoder;
    private final BoardService boardService;

    @Autowired
    public Integration(ClubMemberRepository clubMemberRepository,
                       PasswordEncoder passwordEncoder,
                       BoardService boardService) {
        this.clubMemberRepository = clubMemberRepository;
        this.passwordEncoder = passwordEncoder;
        this.boardService = boardService;
    }


    @Test
    @Transactional
    //@Rollback(value = false)
    @Commit
    public void boardMemberTest() {

        IntStream.rangeClosed(1,10).forEach(i -> {
            ClubMember clubMember = ClubMember.builder()
                    .email("user" + i + "@sh.sh")
                    .name("sh" + i)
                    .fromSocial(false)
                    .password(passwordEncoder.encode("1111") )
                    .build();

            clubMember.addMemberRole(ClubMemberRole.USER);

            if (i > 5) {
                clubMember.addMemberRole(ClubMemberRole.MANAGER);
            }
            if (i > 8) {
                clubMember.addMemberRole(ClubMemberRole.ADMIN);
            }
            clubMemberRepository.save(clubMember);
        });

        // db에서 가져온 특정 계정의 권한 확인
        ClubMember clubMember = clubMemberRepository.findByEmail("user9@sh.sh", false)
                .orElseThrow( () -> new IllegalArgumentException("") );

        System.out.println("불러온 계정의 아이디 " + clubMember);
        System.out.println("불러온 계정의 권한 : " + clubMember.getRoleSet()); // [ADMIN, MANAGER, USER]

        // 해시값과 비밀번호가 일치하는지 확인
        boolean matchResult = passwordEncoder.matches("1111", clubMember.getPassword());
        System.out.println("저장한 비밀번호와 해시함수로 저장된 비밀번호가 일치하는가? : " + matchResult);

        // 10개의 저장된 회원계정을 아이디로 1003개의 게시글 작성
        IntStream.rangeClosed(1,1003).forEach(i -> {

            Board board = Board.builder()
                    .title("Title..." + i)
                    .content("content..." + i)
                    .writer(clubMemberRepository.getReferenceById("user"+( (int)(Math.random() * 10) + 1)+"@sh.sh"))
                    .build();
            // 수동매핑
            BoardDTO boardDTO = BoardDTO.builder()
                    .title(board.getTitle())
                    .content(board.getContent())
                    .writer(board.getWriter())
                    .build();
            boardService.register(boardDTO);

        });


    }
}
