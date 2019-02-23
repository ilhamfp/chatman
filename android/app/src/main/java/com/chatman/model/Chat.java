package com.chatman.model;

import java.util.Date;

public class Chat {
    private String idSender;
    private String nameSender;
    private String idReceiver;
    private Date date;
    private String message;

    public Chat(String idSender, String nameSender, String idReceiver, Date date, String message) {
        this.idSender = idSender;
        this.nameSender = nameSender;
        this.idReceiver = idReceiver;
        this.date = date;
        this.message = message;
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getNameSender() {
        return nameSender;
    }

    public void setNameSender(String nameSender) {
        this.nameSender = nameSender;
    }

    public String getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(String idReceiver) {
        this.idReceiver = idReceiver;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
