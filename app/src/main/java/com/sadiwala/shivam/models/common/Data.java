package com.sadiwala.shivam.models.common;

import java.util.Date;



public class Data extends CodeName {
    private long duration;
    private Date date;
    private long timestamp;
    private String appLink;
    private String launchType;
    private boolean showShareBottomSheet = false;

    public Data(String code, String name, String type, String appLink) {
        super(code, name, type);
        this.appLink = appLink;
    }

    public Data() {

    }

    public long getDuration() {
        return duration;
    }

    public Date getDate() {
        return date;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    public String getLaunchType() {
        return launchType;
    }

    public boolean isShowShareBottomSheet() {
        return showShareBottomSheet;
    }

    public void setShowShareBottomSheet(boolean showShareBottomSheet) {
        this.showShareBottomSheet = showShareBottomSheet;
    }
}
