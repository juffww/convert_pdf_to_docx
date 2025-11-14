package model.bean;

import java.sql.Timestamp;

public class conversionQueueItem {
    private int queueId;
    private int fileId;
    private String status; // PENDING, PROCESSING, COMPLETED, FAILED
    private Timestamp createdAt;
    private Timestamp startedAt;
    private Timestamp completedAt;
    private String errorMessage;

    public conversionQueueItem() {
    }

    public conversionQueueItem(int fileId) {
        this.fileId = fileId;
        this.status = "PENDING";
    }

    public conversionQueueItem(int queueId, int fileId, String status,
                               Timestamp createdAt, Timestamp startedAt,
                               Timestamp completedAt, String errorMessage) {
        this.queueId = queueId;
        this.fileId = fileId;
        this.status = status;
        this.createdAt = createdAt;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.errorMessage = errorMessage;
    }

    public int getFileId() {
        return fileId;
    }

    public int getQueueId() {
        return queueId;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getCompletedAt() {
        return completedAt;
    }

    public Timestamp getStartedAt() {
        return startedAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setStartedAt(Timestamp startedAt) {
        this.startedAt = startedAt;
    }

    public void setCompletedAt(Timestamp completedAt) {
        this.completedAt = completedAt;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
