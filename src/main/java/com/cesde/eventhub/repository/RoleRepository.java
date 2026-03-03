package com.cesde.eventhub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cesde.eventhub.entity.Role;
import com.cesde.eventhub.enums.UserRoles;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByNameRole(UserRoles name);
}
