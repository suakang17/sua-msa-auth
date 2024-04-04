package com.sua.authserver.global.filter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sua.authserver.global.exception.AuthorizationException;
import com.sua.authserver.global.jwt.Jwt;
import com.sua.authserver.global.jwt.JwtProvider;
import com.sua.authserver.member.constant.Role;
import com.sua.authserver.member.dto.MemberLoginResponseDto;
import com.sua.authserver.member.entity.AuthenticateMember;
import com.sua.authserver.member.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter implements Filter {

    private final String[] whiteListUris = new String[]{"/main", "/members/duplicate-check", "/signup", "/login", "/auth/refresh/token", "/member/**"};

    private final JwtProvider jwtProvider;

    private final ObjectMapper objectMapper;

    private final MemberService memberService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("on JwtAuthorizationFilter");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            log.info("Header: {} = {}", headerName, httpServletRequest.getHeader(headerName));
        }

        if (isStaticResource(httpServletRequest.getRequestURI())) {
            log.info("isStaticResource");
            chain.doFilter(request, response);
            return;
        }
        if (whiteListCheck(httpServletRequest.getRequestURI())) {
            log.info("whiteListChecked");
            chain.doFilter(request, response);
            return;
        }
        if (!isContainToken(httpServletRequest)) {
            log.error("token issue");
            httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "인증 오류");
            return;
        }
        try {
            String token = getToken(httpServletRequest);
            AuthenticateMember authenticateMember = getAuthenticateMember(token);
            verifyAuthorization(httpServletRequest.getRequestURI(), authenticateMember);

            chain.doFilter(request, response);
        } catch (JsonParseException e) {
            log.error("JsonParseException");
            httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value());
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException e) {
            log.error("JwtException");
            httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "인증 오류");
        } catch (ExpiredJwtException e) {
            log.error("JwtTokenExpired");
            httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(), "token expired");
        } catch (AuthorizationException e) {
            log.error("AuthorizationException");
            httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "not allowed role");
        }
    }


    private boolean whiteListCheck(String uri) {
        return PatternMatchUtils.simpleMatch(whiteListUris, uri);
    }

    private boolean isContainToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        log.info("request.getHeaderAuthorization={}", request.getHeader("Authorization"));
        return authorization != null && authorization.startsWith("Bearer ");
    }

    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        return authorization.substring(7);
    }

    private AuthenticateMember getAuthenticateMember(String token) throws JsonProcessingException {
        Claims claims = jwtProvider.getClaims(token);
        String authenticateMemberJson = claims.get(VerifyMemberFilter.AUTHENTICATE_MEMBER, String.class);
        return objectMapper.readValue(authenticateMemberJson, AuthenticateMember.class);
    }

    private void verifyAuthorization(String uri, AuthenticateMember member) {
        if (PatternMatchUtils.simpleMatch("*/admin*", uri) && !member.getRoles().contains(Role.ADMIN)) {
            throw new AuthorizationException();
        }
    }

    private boolean isStaticResource(String uri) {
        return uri.endsWith(".js") || uri.endsWith(".css") || uri.endsWith(".png") || uri.endsWith(".jpg") || uri.endsWith(".jpeg") || uri.endsWith(".gif");
    }
}
