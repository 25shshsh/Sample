package com.example.sample.member.repository;

import com.example.sample.member.entity.ClubMember;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubMemberRepository extends JpaRepository<ClubMember, String> {

    // 소셜 로그인 사용자를 구분하기 위함.
    // @EntityGraph 트랜잭셔널 없이 래프트 아우터 조인( 롤셋을 조인 )
    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select m from ClubMember m where m.fromSocial = :social and m.email =:email")
    Optional<ClubMember> findByEmail(@Param("email") String email, @Param("social") boolean social);


}
