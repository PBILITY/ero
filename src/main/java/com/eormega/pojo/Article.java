package com.eormega.pojo;

import java.util.Date;

public class Article {
    private Integer id;

    private String code;

    private String title;

    private String coverImg;

    private String context;

    private Integer readNum;

    private Integer authorId;

    private String authorName;

    private String authorHeadPortrait;

    private Long integral;

    private String tags;

    private String tagIds;

    private String filePath;

    private String baiduCloudPath;

    private String baiduCloudPassword;

    private Byte deleteStatus;

    private Date createDate;

    private Date updateDate;

    public Article(Integer id, String code, String title, String coverImg, String context, Integer readNum, Integer authorId, String authorName, String authorHeadPortrait, Long integral, String tags, String tagIds, String filePath, String baiduCloudPath, String baiduCloudPassword, Byte deleteStatus, Date createDate, Date updateDate) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.coverImg = coverImg;
        this.context = context;
        this.readNum = readNum;
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorHeadPortrait = authorHeadPortrait;
        this.integral = integral;
        this.tags = tags;
        this.tagIds = tagIds;
        this.filePath = filePath;
        this.baiduCloudPath = baiduCloudPath;
        this.baiduCloudPassword = baiduCloudPassword;
        this.deleteStatus = deleteStatus;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public Article() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg == null ? null : coverImg.trim();
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context == null ? null : context.trim();
    }

    public Integer getReadNum() {
        return readNum;
    }

    public void setReadNum(Integer readNum) {
        this.readNum = readNum;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName == null ? null : authorName.trim();
    }

    public String getAuthorHeadPortrait() {
        return authorHeadPortrait;
    }

    public void setAuthorHeadPortrait(String authorHeadPortrait) {
        this.authorHeadPortrait = authorHeadPortrait == null ? null : authorHeadPortrait.trim();
    }

    public Long getIntegral() {
        return integral;
    }

    public void setIntegral(Long integral) {
        this.integral = integral;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags == null ? null : tags.trim();
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds == null ? null : tagIds.trim();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }

    public String getBaiduCloudPath() {
        return baiduCloudPath;
    }

    public void setBaiduCloudPath(String baiduCloudPath) {
        this.baiduCloudPath = baiduCloudPath == null ? null : baiduCloudPath.trim();
    }

    public String getBaiduCloudPassword() {
        return baiduCloudPassword;
    }

    public void setBaiduCloudPassword(String baiduCloudPassword) {
        this.baiduCloudPassword = baiduCloudPassword == null ? null : baiduCloudPassword.trim();
    }

    public Byte getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Byte deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}