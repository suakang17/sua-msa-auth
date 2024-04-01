package com.sua.authserver.member.repository;

import com.sua.authserver.member.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRoleRepository extends JpaRepository<MemberRole,Long> {
}