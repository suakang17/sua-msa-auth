package com.sua.authserver.member.dto;

import com.sua.authserver.member.constant.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import jakarta.validation.constraints.NotBlank;


import java.time.LocalDate;

@Getter
@AllArgsConstructor
@ToString
public class MemberRegisterDto {

    @NotBlank
    private String loginId;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    private String email;
    @NotBlank
    private Gender gender;
    @NotBlank
    private LocalDate birth;

    public void encodePassword(PasswordEncoder passwordEncoder) {
        if (StringUtils.isEmpty(password)) {
            return;
        }
        this.password = passwordEncoder.encode(password);
    }
}
