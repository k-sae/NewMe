package com.kareem.newme.Model;

/**
 * Created by kareem on 7/29/17.
 */

public class ChatMessage {
    private String content;

    public ChatMessage(String content) {
        this.content = content;
    }

    public ChatMessage() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
