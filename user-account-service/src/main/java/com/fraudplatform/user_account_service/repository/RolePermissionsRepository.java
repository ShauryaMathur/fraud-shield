package com.fraudplatform.user_account_service.repository;

import com.fraudplatform.user_account_service.entity.Role;
import com.fraudplatform.user_account_service.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolePermissionsRepository extends JpaRepository<RolePermission, Long> {
    List<RolePermission> findAllByRole(Role role);
}
