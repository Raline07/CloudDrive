package com.example.clouddrivetest.Entity;

import javax.persistence.*;

@Entity
public class CustomFile {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private long size;
    private String date;

    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private CustomUser account;

    public CustomFile(String name, long size, String date, String path) {
        this.name = name;
        this.size = size;
        this.date = date;
        this.path = path;
    }

    public CustomFile() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public CustomUser getUser() {
        return account;
    }

    public void setUser(CustomUser user) {
        this.account = user;
    }
}
