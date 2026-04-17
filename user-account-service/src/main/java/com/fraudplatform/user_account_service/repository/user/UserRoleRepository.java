package com.fraudplatform.user_account_service.repository.user;

import com.fraudplatform.user_account_service.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
