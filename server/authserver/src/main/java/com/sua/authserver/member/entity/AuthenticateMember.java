package com.sua.authserver.member.entity;

import com.sua.authserver.member.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

@Getter
@AllArgsConstructor
@ToString
public class AuthenticateMember {
    private String loginId;
    private Set<Role> roles;
}
