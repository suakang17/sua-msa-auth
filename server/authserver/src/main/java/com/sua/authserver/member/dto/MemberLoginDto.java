package com.sua.authserver.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class MemberLoginDto {
    private String loginId;
    private String password;
}
