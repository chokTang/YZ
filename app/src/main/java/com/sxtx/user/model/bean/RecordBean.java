package com.sxtx.user.model.bean;

/**
 * 介紹:
 * 作者:CHOK
 */
public class RecordBean {

    public boolean isSelected = false;
    public boolean isCollected = false;
    public String name;
    public String tag;
    public String time;
    public String uptime;
    public String pic;
    public String giveMark;
    public long videoId;
    public String playNum;
    public int   isFreeOfCharge;   //1免费   0不免费

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public int getIsFreeOfCharge() {
        return isFreeOfCharge;
    }

    public void setIsFreeOfCharge(int isFreeOfCharge) {
        this.isFreeOfCharge = isFreeOfCharge;
    }

    public long getVideoId() {
        return videoId;
    }

    public void setVideoId(long videoId) {
        this.videoId = videoId;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    public String getGiveMark() {
        return giveMark;
    }

    public void setGiveMark(String giveMark) {
        this.giveMark = giveMark;
    }

    public String getPlayNum() {
        return playNum;
    }

    public void setPlayNum(String playNum) {
        this.playNum = playNum;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
