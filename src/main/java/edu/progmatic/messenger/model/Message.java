package edu.progmatic.messenger.model;

import edu.progmatic.messenger.constans.DateFormats;
import edu.progmatic.messenger.constans.Status;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class Message {

    @NotNull
    @Size(min = 1, max = 255)
    private String text;

    private String sender;
    private Status deleted = Status.NEM_TÖRÖLT;

    @DateTimeFormat(pattern = DateFormats.DATE_TIME_FORMAT)
    private LocalDateTime dateTime;
    private static int idCounter;
    private int id;

    public Message(String text, String sender) {
        this.text = text;
        this.sender = sender;
        this.dateTime = LocalDateTime.now().withNano(0);
        this.id = idCounter++;
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
