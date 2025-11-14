package model.bean;

import java.sql.Timestamp;

public class file {
    private int fileId;
    private int userId;
    private String originalFilename;
    private String filePath;
    private long fileSize;
    private Timestamp uploadTime;

    public file() {
    }

    public file(int userId, String filePath, long fileSize, Timestamp uploadTime, String originalFilename, int fileId) {
        this.userId = userId;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.uploadTime = uploadTime;
        this.originalFilename = originalFilename;
        this.fileId = fileId;
    }

    public file(int userId, String originalFilename, String filePath, long fileSize, Timestamp uploadTime) {
        this.userId = userId;
        this.originalFilename = originalFilename;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.uploadTime = uploadTime;
    }

    public int getFileId() {
        return fileId;
    }

    public int getUserId() {
        return userId;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getFilePath() {
        return filePath;
    }

    public Timestamp getUploadTime() {
        return uploadTime;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setUploadTime(Timestamp uploadTime) {
        this.uploadTime = uploadTime;
    }
}
