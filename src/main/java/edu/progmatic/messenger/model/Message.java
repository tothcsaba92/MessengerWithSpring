package edu.progmatic.messenger.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Message {
    public String text;
    public String sender;
    public LocalDateTime dateTime;

    public Message(String text, String sender) {
        this.text = text;
        this.sender = sender;
        this.dateTime = LocalDateTime.now().withNano(0);

    }

    public String getMessage() {
        return text;
    }

    public String getSender() {
        return sender;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}