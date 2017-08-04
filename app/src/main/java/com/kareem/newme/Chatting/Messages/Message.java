package com.kareem.newme.Chatting.Messages;

/**
 * Helper class for providing sample name for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public  class Message {
    public  String senderId;
    public  String content;
    public  String date;

    public Message(String senderId, String content, String date) {
        this.senderId = senderId;
        this.content = content;
        this.date = date;
    }

    public Message() {
    }
}
