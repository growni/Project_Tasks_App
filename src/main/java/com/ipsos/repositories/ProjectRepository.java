package com.ipsos.repositories;

import com.ipsos.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> getProjectById(Long id);
    Optional<Project> findById(Long id);
    @Query("SELECT p FROM Project p WHERE p.user.username = :username")
    List<Project> findByUsername(String username);
}
