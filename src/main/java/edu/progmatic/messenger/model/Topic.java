package edu.progmatic.messenger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author csaba
 */

@Entity
public class Topic {
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "topic")
    @JsonIgnore
    private List<Message> messages = new ArrayList<>();
    private String name;
    @Id
    @GeneratedValue
    private long id;

    public Topic(String name) {
        this.name = name;
    }

    public Topic() {
    }

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

    public void setId(long id) {
        this.id = id;
    }
}
