package edu.progmatic.messenger.model;

import edu.progmatic.messenger.constans.DateFormats;
import edu.progmatic.messenger.constans.Status;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
public class Message {

    @NotNull
    @Size(min = 1, max = 255)
    private String text;

    private String sender;

    @ManyToOne
    private Topic topic;

    private boolean isDeleted ;

    @DateTimeFormat(pattern = DateFormats.DATE_TIME_FORMAT)
    private LocalDateTime dateTime = LocalDateTime.now();
    @Id
    @GeneratedValue
    private long id;

    public Message(String text, String sender) {
        this.text = text;
        this.sender = sender;
    }

    public Message() {
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getText() {
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

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
