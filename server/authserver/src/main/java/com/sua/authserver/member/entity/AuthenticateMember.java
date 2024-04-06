package com.sua.authserver.member.entity;

import com.sua.authserver.member.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Getter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class AuthenticateMember {
    private String loginId;
    private Set<Role> roles;
}
