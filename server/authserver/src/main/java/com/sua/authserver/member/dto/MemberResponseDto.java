package com.sua.authserver.member.dto;

import com.sua.authserver.member.constant.Gender;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class MemberResponseDto {
    private Long id;
    private String loginId;
    private String password;
    private String name;
    private String email;
    private Gender gender;
    private LocalDate birth;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void clearPassword() {
        this.password = "";
    }
}
