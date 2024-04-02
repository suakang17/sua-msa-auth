package com.sua.authserver.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sua.authserver.global.filter.JwtAuthorizationFilter;
import com.sua.authserver.global.filter.JwtFilter;
import com.sua.authserver.global.filter.VerifyMemberFilter;
import com.sua.authserver.global.jwt.JwtProvider;
import com.sua.authserver.member.service.MemberService;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthFilterConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterRegistrationBean verifyMemberFilter(ObjectMapper mapper, MemberService memberService) {
        FilterRegistrationBean<Filter> filterRegistrationBean = new
                FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new VerifyMemberFilter(memberService, mapper));
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/member/login");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean jwtFilter(JwtProvider provider, ObjectMapper mapper, MemberService memberService) {
        FilterRegistrationBean<Filter> filterRegistrationBean = new
                FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new JwtFilter(provider, memberService , mapper));
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/member/login");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean jwtAuthorizationFilter(JwtProvider provider, ObjectMapper mapper) {
        FilterRegistrationBean<Filter> filterRegistrationBean = new
                FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new JwtAuthorizationFilter(provider,mapper));
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/member/login");
        return filterRegistrationBean;
    }
}