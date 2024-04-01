package com.sua.authserver.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sua.authserver.member.dto.MemberLoginDto;
import com.sua.authserver.member.dto.MemberVerifyResponseDto;
import com.sua.authserver.member.entity.AuthenticateMember;
import com.sua.authserver.member.service.MemberService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class VerifyMemberFilter implements Filter {

    public static final String AUTHENTICATE_MEMBER = "authenticateMember";
    private final MemberService memberService;
    private final ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        log.info("on VerifyMemberFilter");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        log.info("request={}", request.getContentType());

        if ((httpServletRequest.getMethod().equals("POST"))) {
            try {
                String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                log.info("Request Body: {}", requestBody);

                StringReader stringReader = new StringReader(requestBody);
                MemberLoginDto memberLoginDto = objectMapper.readValue(stringReader, MemberLoginDto.class);
                log.info("memberLoginDto={}", memberLoginDto.getLoginId());

                MemberVerifyResponseDto memberVerifyResponseDto = memberService.login(memberLoginDto);

                log.info("memberVerifyResponseDto.isNull?={}", memberVerifyResponseDto == null);
                assert memberVerifyResponseDto != null;
                log.info("memberVerifyResponseDto={}", memberVerifyResponseDto.toString());

                if (memberVerifyResponseDto.isValid()) {
                    request.setAttribute(AUTHENTICATE_MEMBER, new AuthenticateMember(memberLoginDto.getLoginId(), memberVerifyResponseDto.getRole()));

                } else {
                    throw new IllegalArgumentException();
                }
                filterChain.doFilter(request, response);

            } catch (Exception e) {
                log.error("member verification failed={}", e.toString());
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value());
            }
        }
    }
}
