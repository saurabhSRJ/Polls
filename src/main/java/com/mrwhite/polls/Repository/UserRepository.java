package com.mrwhite.polls.Repository;

import com.mrwhite.polls.Entity.db.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
    User findByEmailOrUsername(String username, String email);
    Boolean existsByEmail(String email);
}
