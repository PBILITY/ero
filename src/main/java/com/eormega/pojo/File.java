package com.eormega.pojo;

import java.util.Date;

public class File {
    private Integer id;

    private String fileOldName;

    private String fileName;

    private String path;

    private Date createDate;

    public File(Integer id, String fileOldName, String fileName, String path, Date createDate) {
        this.id = id;
        this.fileOldName = fileOldName;
        this.fileName = fileName;
        this.path = path;
        this.createDate = createDate;
    }

    public File() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileOldName() {
        return fileOldName;
    }

    public void setFileOldName(String fileOldName) {
        this.fileOldName = fileOldName == null ? null : fileOldName.trim();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}