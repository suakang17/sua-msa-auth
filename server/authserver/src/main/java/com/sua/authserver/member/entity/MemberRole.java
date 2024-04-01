package com.sua.authserver.member.entity;

import com.sua.authserver.member.constant.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class MemberRole {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long memberRoleId;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Builder
    public MemberRole(Member member ,Role role){
        this.member = member;
        this.role = role;
    }
}
