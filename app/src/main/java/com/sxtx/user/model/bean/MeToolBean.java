package com.sxtx.user.model.bean;

/**
 * 介紹:
 * 作者:CHOK
 */
public class MeToolBean {
    public int topIcon;
    public String text;
    public boolean isNewMessage;

    public boolean isNewMessage() {
        return isNewMessage;
    }

    public void setNewMessage(boolean newMessage) {
        isNewMessage = newMessage;
    }

    public int getTopIcon() {
        return topIcon;
    }

    public void setTopIcon(int topIcon) {
        this.topIcon = topIcon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
