package com.example.clouddrivetest.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.clouddrivetest.Entity.CustomUser;

public interface UserRepository extends JpaRepository<CustomUser, Long> {
    CustomUser findByLogin(String login);
    boolean existsByLogin(String login);
}
