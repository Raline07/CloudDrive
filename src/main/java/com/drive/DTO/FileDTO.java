package com.drive.DTO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.drive.Entity.CustomFile;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class FileDTO {

    private String name;
    private Long size;
    private String date;

    @JsonCreator
    public FileDTO(@JsonProperty(required = true) String name,
                   @JsonProperty(required = true) Long size,
                   @JsonProperty(required = true) String date) {
        this.name = name;
        this.size = size;
        this.date = date;
    }

    public static FileDTO from(CustomFile customFile) {
        return new FileDTO(customFile.getName(), customFile.getSize(), customFile.getDate());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
