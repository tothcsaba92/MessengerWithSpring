package edu.progmatic.messenger.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Topic {
    @OneToMany(mappedBy = "topic")
    private List<Message> messages = new ArrayList<>();

    private String name;
    @Id
    @GeneratedValue
    private long id;

    public Topic(String name) {
        this.name = name;
    }

    public Topic(){}

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

}
