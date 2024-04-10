package com.sua.authserver.member.dto;

import com.sua.authserver.member.constant.Gender;
import com.sua.authserver.member.constant.Role;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder
public class MemberLoginResponseDto {
    private Long id;
    private String loginId;
    private String name;
    private String email;
    private Gender gender;
    private boolean isAdmin;
    private LocalDate birth;
}
