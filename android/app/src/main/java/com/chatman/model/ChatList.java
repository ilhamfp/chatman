package com.chatman.model;

public class ChatList {
    private int avatarResource;
    private String name;
    private String lastMessage;
    // Todo : tambahin atribut KEY atau apalah supaya ntar waktu ditekan, ke fragmentnya bisa dipass KEY itu terus data di dalam fragmentnya diambil berdasarkan KEY tersebut

    public ChatList() {
    }

    public ChatList(int avatarResource, String name, String lastMessage) {
        this.avatarResource = avatarResource;
        this.name = name;
        this.lastMessage = lastMessage;
    }

    public int getAvatarResource() {
        return avatarResource;
    }

    public void setAvatarResource(int avatarResource) {
        this.avatarResource = avatarResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
