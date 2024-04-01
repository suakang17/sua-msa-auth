package com.sua.authserver.global.tools;

import com.sua.authserver.member.dto.MemberResponseDto;
import com.sua.authserver.member.entity.Member;

public class MemberToResponseConverter {
    public MemberResponseDto convert(Member member) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .name(member.getLoginId())
                .password(member.getPassword())
                .name(member.getName())
                .email(member.getEmail())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}
