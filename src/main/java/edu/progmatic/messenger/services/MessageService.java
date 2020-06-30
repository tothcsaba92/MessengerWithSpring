package edu.progmatic.messenger.services;

import edu.progmatic.messenger.controllers.MessageController;
import edu.progmatic.messenger.model.Message;
import edu.progmatic.messenger.model.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class MessageService {

    @PersistenceContext
    EntityManager em;

    Logger logger = LoggerFactory.getLogger(MessageController.class);


    public List<Message> showMessages(String order, Integer limit, String direction) {
        switch(order){
            case "time":
                order = "date_time";
                break;
            case "message":
                order = "text";
                break;
            default:
                order = "sender";
                break;
        }
        if ("desc".equals(direction)) {
            direction = "desc";
        } else {
            direction = "asc";
        }


        List<Message> resultList = em.createQuery(
                "SELECT m FROM Message m WHERE m.isDeleted = : isDeleted1 OR m.isDeleted = : isDeleted2  ORDER BY " + order + " " + direction)
                .setParameter("isDeleted1", false).setParameter("isDeleted2",true)
                .setMaxResults(limit)
                .getResultList();
        return resultList;
    }

    public List<Message> showNonDeletedMessages(String order, Integer limit, String direction) {
        //TODO adatbazisos cucc,csak raszurni a deleted oszlopra
        switch(order){
            case "time":
                order = "date_time";
                break;
            case "message":
                order = "text";
                break;
            default:
                order = "sender";
                break;
        }
        if ("desc".equals(direction)) {
            direction = "desc";
        } else {
            direction = "asc";
        }


        List<Message> resultList = em.createQuery(
                "SELECT m FROM Message m WHERE m.isDeleted = :isDeleted ORDER BY " + order + " " + direction)
                .setParameter("isDeleted",false)
                .setMaxResults(limit)
                .getResultList();

        return resultList;
    }

    @Transactional
    public Message showSelectedMessageById(int msgId) {
       return (Message) em.createQuery(
                "SELECT m FROM Message m WHERE m.id = :msgId")
                .setParameter("msgId",msgId)
                .getResultList().get(0);
    }
    @Transactional
    public void createNewMessage(Message newMessage) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newMessage.setSender(user.getUsername());
        em.persist( newMessage);
    }

    @Transactional
    public void createNewTopic(String name){
        Topic topic = new Topic();
        topic.setName(name);
        em.persist(topic);
    }


    @Transactional
    public void setMessageForDeletion(long id){
//        em.createQuery("SELECT m, CASE m.isDeleted WHEN TRUE THEN FALSE" +
//                " WHEN FALSE THEN TRUE ELSE m.isDeleted END from Message m where m.id = : msgId")
//        .setParameter("msgId",id);

            Message m = em.find(Message.class, id);
            if(m.isDeleted()){
                m.setDeleted(false);
            } else {
                m.setDeleted(true);
            }


    }

}
