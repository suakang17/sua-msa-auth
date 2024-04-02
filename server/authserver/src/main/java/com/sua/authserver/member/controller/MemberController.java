package com.sua.authserver.member.controller;

import com.sua.authserver.global.jwt.Jwt;
import com.sua.authserver.member.constant.Role;
import com.sua.authserver.member.dto.*;
import com.sua.authserver.member.entity.AuthenticateMember;
import com.sua.authserver.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/main")
    public ResponseEntity<String> mainPage() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity saveMember(@RequestBody MemberRegisterDto memberRegisterDto) {
        memberService.signUp(memberRegisterDto);
        return new ResponseEntity(HttpStatus.OK);
    }
//
//    @PostMapping("/login")
//    public MemberResponseDto verifyMemberRole(@RequestBody MemberLoginDto memberLoginDto) {
////        AuthenticateMember member = (AuthenticateMember) request.getHeaders("AUTHENTICATE_MEMBER");
////        log.info("AuthenticateMember={}", member.toString());
//        MemberVerifyResponseDto verifyDto = memberService.login(memberLoginDto);
//        if (verifyDto.isValid()) return memberService.findMemberByEmail(memberLoginDto.getLoginId());
//        return null;
//    }


//    @PostMapping("/refresh/token")
//    public ResponseEntity<Jwt> tokenRefresh(@RequestBody TokenRefreshDto tokenRefreshDto) {
//        Jwt jwt = memberService.refreshToken(tokenRefreshDto.getRefreshToken());
//        if (jwt == null){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(null);
//        }
//        return ResponseEntity.ok(jwt);
//    }
}
