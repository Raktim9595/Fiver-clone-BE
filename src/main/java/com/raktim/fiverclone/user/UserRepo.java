package com.raktim.fiverclone.user;

import com.raktim.fiverclone.user.model.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, UUID> {
    UserEntity findByEmail(String email);
    // Load the role in the same SQL query (JOIN) instead of lazily afterwards. The security
    // filter reads the role outside any open Hibernate session, where a lazy load would fail.
    @EntityGraph(attributePaths = "role")
    UserEntity findByUsername(String username);
}
