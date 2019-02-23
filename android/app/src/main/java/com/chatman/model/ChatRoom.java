package com.chatman.model;

import java.util.List;

public class ChatRoom {
    private String idSender;
    private List<Chat> messages;

    public ChatRoom(String idSender, List<Chat> messages) {
        this.idSender = idSender;
        this.messages = messages;
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public List<Chat> getMessages() {
        return messages;
    }

    public void setMessages(List<Chat> messages) {
        this.messages = messages;
    }
}
