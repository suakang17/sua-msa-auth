package com.sua.authserver.member.entity;

import com.sua.authserver.member.constant.Gender;
import com.sua.authserver.member.constant.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;
    private String password;
    private String name;
    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDate birth;

    @OneToMany(mappedBy = "member",cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<MemberRole> memberRoles = new HashSet<>();
    private String refreshToken;

    @Builder
    protected Member(String loginId, String password, String name, String email, Gender gender, LocalDate birth) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.birth = birth;
    }

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void updateMember(String password, String email, Gender gender, LocalDate birth) {
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.birth = birth;
    }

    public void addRole(MemberRole memberRole){
        memberRoles.add(memberRole);
    }
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
