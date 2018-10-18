package com.eormega.pojo;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private Integer id;

    private String username;

    private String password;

    private String nickName;

    private String headPortrait;

    private Integer integral;

    private Date createDate;

    private Integer role;

    public User(Integer id, String username, String password, String nickName, String headPortrait, Integer integral, Date createDate, Integer role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickName = nickName;
        this.headPortrait = headPortrait;
        this.integral = integral;
        this.createDate = createDate;
        this.role = role;
    }

    public User() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait == null ? null : headPortrait.trim();
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}