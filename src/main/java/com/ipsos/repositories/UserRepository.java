package com.ipsos.repositories;

import com.ipsos.entities.Project;
import com.ipsos.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getByUsername(String username);
    Optional<User> findById(Long userId);
    Optional<Project> getProjectById(Long projectId);
    User getUserByUsername(String username);

}
