package com.sua.authserver.member.repository;

import com.sua.authserver.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Override
    <S extends Member> S save(S member);

    Member findByLoginId(String loginId);

    int countByLoginId(String loginId);

    Member findByRefreshToken(String refreshToken);

    Member findByMemberEmail(String memberEmail);
}