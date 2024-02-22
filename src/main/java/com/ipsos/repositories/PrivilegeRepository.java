package com.ipsos.repositories;

import com.ipsos.entities.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Privilege findByName(String name);
}
