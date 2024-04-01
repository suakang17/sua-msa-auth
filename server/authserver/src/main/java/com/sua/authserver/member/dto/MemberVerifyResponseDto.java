package com.sua.authserver.member.dto;

import com.sua.authserver.member.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@AllArgsConstructor
@ToString
public class MemberVerifyResponseDto {
    private boolean isValid;
    private Role role;
}