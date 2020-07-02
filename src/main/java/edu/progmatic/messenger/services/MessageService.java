package edu.progmatic.messenger.services;

import edu.progmatic.messenger.controllers.TopicController;
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
import java.util.List;

@Service
public class MessageService {

    @PersistenceContext
    EntityManager em;
    Logger logger = LoggerFactory.getLogger(MessageService.class);
    public List<Message> showMessagesForAdmin(String order, Long limit, String direction,Long topicId, boolean isDeleted) {
        orderBySelect(order);
        orderDirectionSelect(direction);
        logger.info(topicId+" serviceben bent az id");
        logger.info(isDeleted+"serviceben a boolean");
        if(topicId != null){
            return em.createQuery(
                    "SELECT m FROM Message m WHERE m.topic.id = :topicId AND m.isDeleted = :isDeleted" +
                            " ORDER BY " + order + " " + direction)
                    .setParameter("isDeleted", isDeleted).setParameter("topicId",topicId)
                    .setMaxResults(Math.toIntExact(limit))
                    .getResultList();
        } else{
            logger.info("topic id null");
           return em.createQuery("SELECT m FROM Message m WHERE m.isDeleted = :isDeleted ORDER BY " + order + " " + direction)
                    .setParameter("isDeleted", isDeleted)
                    .setMaxResults(Math.toIntExact(limit))
                    .getResultList();
        }
    }

    public List<Message> showMessagesForUser(String order, Long limit, String direction,Long topicId) {
        orderBySelect(order);
        orderDirectionSelect(direction);
        return em.createQuery(
                "SELECT m FROM Message m WHERE m.isDeleted = :isDeleted AND m.topic.id = :topicId ORDER BY " + order + " " + direction)
                .setParameter("isDeleted", false).setParameter("topicId",topicId)
                .setMaxResults(Math.toIntExact(limit))
                .getResultList();
    }

    @Transactional
    public Message showSelectedMessageById(Long msgId) {
        return (Message) em.createQuery(
                "SELECT m FROM Message m WHERE m.id = :msgId")
                .setParameter("msgId", msgId)
                .getResultList().get(0);
    }

    @Transactional
    public void createNewMessage(Message newMessage) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newMessage.setSender(user.getUsername());
        Topic topic = em.find(Topic.class, newMessage.getTopic().getId());
        newMessage.setTopic(topic);
        em.persist(newMessage);
    }

    @Transactional
    public void setMessageForDeletion(long id) {
        Message m = em.find(Message.class, id);
        m.setDeleted(!m.isDeleted());
    }

    public String orderBySelect(String order) {
        if (order.equals("time")) {
            order = "date_time";
        } else if (order.equals("message")) {
            order = "text";
        } else {
            order = "sender";
        }
        return order;
    }

    public String orderDirectionSelect(String direction) {
        if (direction.equals("desc")) {
            return "desc";
        } else {
            return "asc";
        }
    }

}
