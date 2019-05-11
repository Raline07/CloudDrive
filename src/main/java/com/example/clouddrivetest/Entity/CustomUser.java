package com.example.clouddrivetest.Entity;

import com.example.clouddrivetest.UserRole;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class CustomUser {
    @Id
    @GeneratedValue
    private Long id;

    private String login;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private boolean isUploading = false;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<CustomFile> files = new LinkedList<>();

    public CustomUser(String login, String password, UserRole role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public CustomUser() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public List<CustomFile> getFiles() {
        return files;
    }

    public CustomFile getFile(Long id) {
        for (CustomFile f : files) {
            if (f.getId().equals(id))
                return f;
        }
        return null;
    }

    public void addFiles(List<CustomFile> files) {
        this.files.addAll(files);
    }

    public boolean isUploading() {
        return isUploading;
    }

    public void setUploading(boolean uploading) {
        isUploading = uploading;
    }
}
