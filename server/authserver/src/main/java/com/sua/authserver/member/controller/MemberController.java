package com.sua.authserver.member.controller;

import com.sua.authserver.global.jwt.Jwt;
import com.sua.authserver.member.constant.Role;
import com.sua.authserver.member.dto.MemberRegisterDto;
import com.sua.authserver.member.dto.TokenRefreshDto;
import com.sua.authserver.member.entity.AuthenticateMember;
import com.sua.authserver.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<String> mainPage() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/signup")
    @ResponseBody
    public void saveMember(@RequestBody MemberRegisterDto memberRegisterDto) {
        memberService.signUp(memberRegisterDto);
    }

    @GetMapping("/login")
    public ResponseEntity<String> verifyMemberRole(HttpServletRequest request) {
        AuthenticateMember member = (AuthenticateMember) request.getHeaders("AUTHENTICATE_MEMBER");
        log.info("AuthenticateMember={}", member.toString());

        String redirectUrl;
        if(member.getRole().equals(Role.ADMIN)) {
            // 회원이 ADMIN 권한을 가지고 있다면 회원 관리 페이지로 리다이렉트
            redirectUrl = "/admin/member-management";
        } else if (member.getRole().equals(Role.MEMBER)) {
            // 회원이 USER 권한을 가지고 있다면 회원 정보 관리 페이지로 리다이렉트
            redirectUrl = "/member/view";
        } else {
            // 권한이 없는 경우 등에 따른 처리
            redirectUrl = "/login";
        }

        // 리다이렉트
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", redirectUrl)
                .body(null);
    }


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
