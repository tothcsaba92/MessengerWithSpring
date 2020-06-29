package edu.progmatic.messenger.model;

import edu.progmatic.messenger.constans.DateFormats;
import edu.progmatic.messenger.constans.Status;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name="MESSAGE")
public class Message {
    @Column(name="TEXT")
    @NotNull
    @Size(min = 1, max = 255)
    private String text;

    @Column(name="SENDER")
    private String sender;

    @Column(name="DELETED")
    private Status deleted = Status.NEM_TOROLT;

    @Column(name="DATE_TIME")
    @DateTimeFormat(pattern = DateFormats.DATE_TIME_FORMAT)
    private LocalDateTime dateTime;

    @Id
    @GeneratedValue
    private int id;

    private Message(){

    }

    public Message(String text, String sender) {
        this.text = text;
        this.sender = sender;
        this.dateTime = LocalDateTime.now().withNano(0);
    }

    public Status getDeleted() {
        return deleted;
    }

    public void setDeleted(Status deleted) {
        this.deleted = deleted;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
