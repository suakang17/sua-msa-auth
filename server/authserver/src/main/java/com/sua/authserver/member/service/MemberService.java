package com.sua.authserver.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sua.authserver.global.filter.VerifyMemberFilter;
import com.sua.authserver.global.jwt.Jwt;
import com.sua.authserver.global.tools.MemberToResponseConverter;
import com.sua.authserver.member.constant.Role;
import com.sua.authserver.member.dto.*;
import com.sua.authserver.global.jwt.JwtProvider;
import com.sua.authserver.member.entity.AuthenticateMember;
import com.sua.authserver.member.entity.Member;
import com.sua.authserver.member.entity.MemberRole;
import com.sua.authserver.member.repository.MemberRepository;
import com.sua.authserver.member.repository.MemberRoleRepository;
import com.sua.authserver.member.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final RefreshTokenRepository refreshTokenRepository;

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

        MemberRole role = MemberRole.builder()
                .role(Role.MEMBER)
                .member(newMember)
                .build();
        newMember.addRole(role);

        log.info("password= {}", memberRegisterDto.getPassword());
        log.info("role={}", newMember.getMemberRoles().contains(Role.MEMBER));
        memberRepository.save(newMember);
        memberRoleRepository.save(role);

        log.info("complete save");
        return newMember.getId();
    }

    public MemberVerifyResponseDto verifyMember(MemberLoginDto memberLoginDto) {
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
                    .role(member.getMemberRoles().stream().map(MemberRole::getRole).collect(Collectors.toSet())).build();
        } else {
            log.info("member is empty");
            return MemberVerifyResponseDto.builder()
                    .isValid(false)
                    .build();
        }
    }

    public MemberResponseDto findMemberByEmail(String memberEmail) {
        return converter.convert(memberRepository.findByEmail(memberEmail));
    }

    @Transactional
    public void updateRefreshToken(String memberEmail, String refreshToken) {
        Member member = memberRepository.findByEmail(memberEmail);
        if (member == null)
            return;
        member.updateRefreshToken(refreshToken);
    }

    @Transactional
    public boolean addMemberRole(String email, Role role) {
        Member member = memberRepository.findByEmail(email);
        if (member.getMemberRoles().stream().anyMatch(memberRole -> memberRole.getRole().equals(role)))
            return false;
        MemberRole memberRole = MemberRole.builder()
                .member(member)
                .role(role)
                .build();
        member.addRole(memberRole);
        memberRoleRepository.save(memberRole);
        return true;
    }

    @Transactional
    public Jwt refreshToken(String refreshToken){
        try{
            jwtProvider.getClaims(refreshToken);
            Member member = memberRepository.findByRefreshToken(refreshToken);
            if(member == null)
                return null;

            HashMap<String, Object> claims = new HashMap<>();
            AuthenticateMember authenticateMember = new AuthenticateMember(member.getLoginId(),
                    member.getMemberRoles().stream().map(MemberRole::getRole).collect(Collectors.toSet()));
            String authenticateUserJson = objectMapper.writeValueAsString(authenticateMember);
            claims.put(VerifyMemberFilter.AUTHENTICATE_MEMBER,authenticateUserJson);
            Jwt jwt = jwtProvider.createJwt(claims);
            updateRefreshToken(member.getEmail(),jwt.getRefreshToken());
            return jwt;
        } catch (Exception e){
            return null;
        }
    }

    public AuthenticateMember login(MemberLoginDto loginDto) {
        Member member = memberRepository.findByLoginId(loginDto.getLoginId());
        return new AuthenticateMember(member.getLoginId(), member.getMemberRoles().stream().map(MemberRole::getRole).collect(Collectors.toSet()));
    }

    public MemberLoginResponseDto findMemberByLoginId(String loginId) {
        Member member = memberRepository.findByLoginId(loginId);

        return MemberLoginResponseDto.builder()
                .id(member.getId())
                .loginId(member.getLoginId())
                .name(member.getName())
                .email(member.getEmail())
                .gender(member.getGender())
                .isAdmin(member.getMemberRoles().contains(Role.ADMIN))
                .birth(member.getBirth())
                .build();
    }

    public void logout(HttpServletRequest request) throws JsonProcessingException {
        String accessToken = jwtProvider.getToken(request);
        ObjectMapper mapper = new ObjectMapper();

        String member = jwtProvider.getClaims(accessToken).get("authenticateMember", String.class);
        AuthenticateMember am = mapper.readValue(member, AuthenticateMember.class);

        if (refreshTokenRepository.hasKey(am.getLoginId())) {
            refreshTokenRepository.deleteRefreshToken(am.getLoginId());
        }

        // TODO 회원가입시 refresh는 redis에 저장하도록 변경

        refreshTokenRepository.setBlackList(accessToken, "logout", jwtProvider.getExpireDateAccessToken().getTime());

    }
}
