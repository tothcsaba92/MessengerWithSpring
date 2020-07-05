package edu.progmatic.messenger.dto;

import edu.progmatic.messenger.model.Topic;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MessageDTO {
    @NotNull
    @Size(min = 1, max = 255)
    private String text;
    private String sender;
    private Topic topic;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}
