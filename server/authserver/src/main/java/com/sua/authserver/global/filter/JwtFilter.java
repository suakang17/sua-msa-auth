package com.sua.authserver.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sua.authserver.global.jwt.Jwt;
import com.sua.authserver.global.jwt.JwtProvider;
import com.sua.authserver.member.entity.AuthenticateMember;
import com.sua.authserver.member.service.MemberService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter implements Filter {

    private final JwtProvider jwtProvider;
    private final MemberService memberService;
    private final ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        log.info("on JwtFilter");
        Object attribute = request.getAttribute(VerifyMemberFilter.AUTHENTICATE_MEMBER);
        log.info("request.getAttribute(VerifyMemberFilter.AUTHENTICATE_MEMBER={}", VerifyMemberFilter.AUTHENTICATE_MEMBER);
        if(attribute instanceof AuthenticateMember authenticateMember) {
            Map<String, Object> claims = new HashMap<>();
            String authenticateUserJson = objectMapper.writeValueAsString(authenticateMember);
            claims.put(VerifyMemberFilter.AUTHENTICATE_MEMBER, authenticateUserJson);
            Jwt jwt = jwtProvider.createJwt(claims);
            memberService.updateRefreshToken(authenticateMember.getLoginId(), jwt.getRefreshToken());
            String json = objectMapper.writeValueAsString(jwt);

            log.info("jwtAT={}", jwt.getRefreshToken());
            log.info("jwtRT={}", jwt.getAccessToken());

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);

            filterChain.doFilter(request, response);
            return;
        }
        log.info("attribute is not instanceof AuthenticateMember");
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
