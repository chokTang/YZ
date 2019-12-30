package com.sxtx.user.model.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 介紹:
 * 作者:CHOK
 */
public class HomeRecBean {
    public int position;
    public String title;
    public List<GcContentBean> list = new ArrayList<>();

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<GcContentBean> getList() {
        return list;
    }

    public void setList(List<GcContentBean> list) {
        this.list = list;
    }
}
