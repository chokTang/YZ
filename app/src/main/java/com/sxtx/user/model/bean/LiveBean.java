package com.sxtx.user.model.bean;

import java.io.Serializable;

/**
 * 介紹:
 * 作者:CHOK
 */
public class LiveBean implements Serializable {

    public int watchTv;//观看人数
    public int isFree;//是否免费
    public int isVip;//是否是vip
    public String bannerImg;////图片
    public String name;//名称
    public String address;//播放地址

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public int getWatchTv() {
        return watchTv;
    }

    public void setWatchTv(int watchTv) {
        this.watchTv = watchTv;
    }

    public int getIsFree() {
        return isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }

    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
