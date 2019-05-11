package com.example.clouddrivetest.Repositories;

import com.example.clouddrivetest.Entity.CustomFile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<CustomFile, Long> {
    List<CustomFile> findByAccountLogin(String login, Pageable pageable);
    Long countByAccountLogin(String login);
    CustomFile findByNameAndAccountLogin(String name, String login);
    void deleteByNameAndAccountLogin(String name, String login);
}
