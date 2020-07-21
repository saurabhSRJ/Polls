package com.mrwhite.polls.Repository;

import com.mrwhite.polls.Entity.db.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(String roleName);
    List<Role> findByIdIn(List<Long> roleIds);
    Boolean existsByRoleName(String roleName);
}
