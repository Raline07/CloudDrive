package com.drive.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.drive.Entity.CustomUser;

public interface UserRepository extends JpaRepository<CustomUser, Long> {
    CustomUser findByLogin(String login);
    boolean existsByLogin(String login);
}
