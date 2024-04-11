package com.sua.authserver.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sua.authserver.global.jwt.Jwt;
import com.sua.authserver.member.dto.*;

import com.sua.authserver.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    public static final String LOGIN_ID = "loginid";


    @GetMapping("/main")
    public ResponseEntity<String> mainPage() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity saveMember(@RequestBody MemberRegisterDto memberRegisterDto) {
        memberService.signUp(memberRegisterDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity login() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/admin")
    public ResponseEntity adminPage() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) throws JsonProcessingException {
        memberService.logout(request);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity memberPage(HttpServletRequest request) {
        String loginId = (String) request.getAttribute(LOGIN_ID);
        MemberLoginResponseDto memberByLoginId = memberService.findMemberByLoginId(loginId);
        return ResponseEntity.ok(memberByLoginId);
    }


    @PostMapping("/refresh/token")
    public ResponseEntity<Jwt> tokenRefresh(@RequestBody TokenRefreshDto tokenRefreshDto) {
        Jwt jwt = memberService.refreshToken(tokenRefreshDto.getRefreshToken());
        if (jwt == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }
        return ResponseEntity.ok(jwt);
    }
}
