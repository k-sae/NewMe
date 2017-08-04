package com.kareem.newme.Chatting.UserMessage;

import com.kareem.newme.Chatting.Messages.Message;

import java.util.ArrayList;

/**
 * Helper class for providing sample name for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
  public  class UserMessage {
    public  String name;
    public  ArrayList<Message> messages = new ArrayList<>();
    public UserMessage(String name) {
        this.name = name;
    }

    public UserMessage() {
    }

    @Override
    public String toString() {
        return name;
    }
}
