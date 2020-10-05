package com.pepivsky.chat.model;

import com.pepivsky.chat.pubnub.PUBNUB;

public class Message {
    private String content;
    private boolean isMine;
    private String publisher;

    public Message(String content, boolean isMine) {
        this.content = content;
        this.isMine = isMine;
    }

    public Message(String content, String publisher) {
        this.content = content;
        this.publisher = publisher;
    }

    public String getPublisher() {
        if (this.publisher.equalsIgnoreCase(PUBNUB.MY_UUID)) {
            return "Yo";
        } else {
            return publisher;
        }

    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
