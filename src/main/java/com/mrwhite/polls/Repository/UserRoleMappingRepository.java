package com.mrwhite.polls.Repository;

import com.mrwhite.polls.Entity.db.UserRoleMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleMappingRepository extends JpaRepository<UserRoleMapping, Long> {
    List<UserRoleMapping> findByUserId(Long userId);
    UserRoleMapping findByUserIdAndRoleId(Long userId,Long roleId);
    @Modifying
    @Query(value = "update user_role_mapping set deleted = true where deleted = false and user_id = :id",nativeQuery = true)
    void deleteByUserId(@Param("id") Long id);
}
