package com.mrwhite.polls.Repository;

import com.mrwhite.polls.Entity.db.UserRoleMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleMappingRepository extends JpaRepository<UserRoleMapping, Long> {
}
