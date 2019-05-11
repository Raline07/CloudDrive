package com.example.clouddrivetest.Zip;

import java.io.InputStream;

public class FileData {

    private String name;
    private long size;
    private InputStream is;

    public FileData(String name, long size, InputStream is) {
        this.name = name;
        this.size = size;
        this.is = is;
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

    public InputStream getIs() {
        return is;
    }

    public void setIs(InputStream is) {
        this.is = is;
    }
}
