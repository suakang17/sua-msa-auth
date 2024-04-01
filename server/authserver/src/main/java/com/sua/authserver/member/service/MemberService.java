package com.sua.authserver.member.service;

import com.sua.authserver.global.filter.VerifyMemberFilter;
import com.sua.authserver.global.jwt.Jwt;
import com.sua.authserver.global.tools.MemberToResponseConverter;
import com.sua.authserver.member.constant.Role;
import com.sua.authserver.member.dto.MemberLoginDto;
import com.sua.authserver.member.dto.MemberRegisterDto;
import com.sua.authserver.member.dto.MemberVerifyResponseDto;
import com.sua.authserver.global.jwt.JwtProvider;
import com.sua.authserver.member.dto.MemberResponseDto;
import com.sua.authserver.member.entity.AuthenticateMember;
import com.sua.authserver.member.entity.Member;
import com.sua.authserver.member.entity.MemberRole;
import com.sua.authserver.member.repository.MemberRepository;
import com.sua.authserver.member.repository.MemberRoleRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;

    private final JwtProvider jwtProvider;

    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final MemberToResponseConverter converter;

    @Transactional
    public Long signUp(MemberRegisterDto memberRegisterDto) {

        log.info("authservice memberRegisterDto= {}", memberRegisterDto.toString());
        memberRegisterDto.encodePassword(passwordEncoder);
        Member newMember = Member.builder()
                .loginId((memberRegisterDto.getLoginId()))
                .password(memberRegisterDto.getPassword())
                .name(memberRegisterDto.getName())
                .email(memberRegisterDto.getEmail())
                .gender(memberRegisterDto.getGender())
                .birth(memberRegisterDto.getBirth())
                .build();

        newMember.setRole(Role.MEMBER);

        log.info("password= {}", memberRegisterDto.getPassword());
        log.info("genderType={}", memberRegisterDto.getGender().getClass());
        memberRepository.save(newMember);
        memberRepository.save(newMember);
        log.info("complete save");
        return newMember.getId();
    }

    public MemberVerifyResponseDto login(MemberLoginDto memberLoginDto) {
        log.info("memberLoginId={}", memberLoginDto.getLoginId());
        Member member = memberRepository.findByLoginId(memberLoginDto.getLoginId());
        String encodedPassword = (member == null) ? "" : member.getPassword();

        if (member != null) {
            log.info("member is present. LoginId={}", member.getLoginId());
            if (!passwordEncoder.matches(memberLoginDto.getPassword(), encodedPassword)) {
                log.info("password not matched");
                return MemberVerifyResponseDto.builder()
                        .isValid(false)
                        .build();
            }
            return MemberVerifyResponseDto.builder()
                    .isValid(true)
                    .role(member.getRole())
                    .build();
        } else {
            log.info("member is empty");
            return MemberVerifyResponseDto.builder()
                    .isValid(false)
                    .build();
        }
    }

    @Transactional
    public boolean changeMemberRole(String loginId, Role role) {
        Member member = memberRepository.findByLoginId(loginId);
        if (member == null) return false;

        if (member.getRole() == Role.MEMBER) {
            member.setRole(Role.ADMIN);
        } else {
            member.setRole(Role.MEMBER);
        }
        return true;
    }

    public MemberResponseDto findMemberByEmail(String MemberEmail) {
        return converter.convert(memberRepository.findByMemberEmail(MemberEmail));
    }

    @Transactional
    public void updateRefreshToken(String MemberEmail, String refreshToken) {
        Member member = memberRepository.findByMemberEmail(MemberEmail);
        if (member == null)
            return;
        member.updateRefreshToken(refreshToken);
    }
//
//    @Transactional
//    public Jwt refreshToken(String refreshToken) {
//        try {
//            // 유효한 토큰 인지 검증
//            jwtProvider.getClaims(refreshToken);
//            Member Member = memberRepository.findByRefreshToken(refreshToken);
//            if (Member == null)
//                return null;
//
//            HashMap<String, Object> claims = new HashMap<>();
//            AuthenticateMember authenticateMember = new AuthenticateMember(Member.getMemberEmail(),
//                    member.getMemberRoles().stream().map(MemberRole::getRole).collect(Collectors.toSet()));
//            String authenticateMemberJson = objectMapper.writeValueAsString(authenticateMember);
//            claims.put(VerifyMemberFilter.AUTHENTICATE_Member, authenticateMemberJson);
//            Jwt jwt = jwtProvider.createJwt(claims);
//            updateRefreshToken(Member.getMemberEmail(), jwt.getRefreshToken());
//            return jwt;
//        } catch (Exception e) {
//            return null;
//        }
//    }

//    @Transactional
//    public boolean addMemberRole(String email, Role role) {
//        Member member = memberRepository.findByMemberEmail(email);
//        if (member.getMemberRoles().stream().anyMatch(memberRole -> memberRole.getRole().equals(role)))
//            return false;
//        MemberRole memberRole = MemberRole.builder()
//                .member(member)
//                .role(role)
//                .build();
//        member.addRole(memberRole);
//        memberRoleRepository.save(memberRole);
//        return true;
//    }
}
