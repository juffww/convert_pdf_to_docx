package model.bean;

import java.sql.Timestamp;

public class convertedFile {
    private int convertedId;
    private int queueId;
    private String outputFilename;
    private String outputPath;
    private long outputSize;
    private int conversionTime;
    private Timestamp createdAt;

    public convertedFile() {
    }

    public convertedFile(int convertedId, int queueId, String outputFilename,
                         String outputPath, long outputSize, int conversionTime,
                         Timestamp createdAt) {
        this.convertedId = convertedId;
        this.queueId = queueId;
        this.outputFilename = outputFilename;
        this.outputPath = outputPath;
        this.outputSize = outputSize;
        this.conversionTime = conversionTime;
        this.createdAt = createdAt;
    }

    public int getConvertedId() {
        return convertedId;
    }

    public void setConvertedId(int convertedId) {
        this.convertedId = convertedId;
    }

    public int getQueueId() {
        return queueId;
    }

    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    public String getOutputFilename() {
        return outputFilename;
    }

    public void setOutputFilename(String outputFilename) {
        this.outputFilename = outputFilename;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public long getOutputSize() {
        return outputSize;
    }

    public void setOutputSize(long outputSize) {
        this.outputSize = outputSize;
    }

    public int getConversionTime() {
        return conversionTime;
    }

    public void setConversionTime(int conversionTime) {
        this.conversionTime = conversionTime;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}