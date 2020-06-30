package edu.progmatic.messenger.services;

import edu.progmatic.messenger.model.Message;
import edu.progmatic.messenger.model.Topic;
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

    public List<Message> showMessagesForAdmin(String order, Long limit, String direction) {
        orderBySelect(order);
        orderDirectionSelect(direction);
        return em.createQuery(
                "SELECT m FROM Message m WHERE m.isDeleted = : isDeleted1 OR m.isDeleted = : isDeleted2  ORDER BY " + order + " " + direction)
                .setParameter("isDeleted1", false).setParameter("isDeleted2", true)
                .setMaxResults(Math.toIntExact(limit))
                .getResultList();
    }

    public List<Message> showMessagesForUser(String order, Long limit, String direction) {
        orderBySelect(order);
        orderDirectionSelect(direction);
        return em.createQuery(
                "SELECT m FROM Message m WHERE m.isDeleted = :isDeleted ORDER BY " + order + " " + direction)
                .setParameter("isDeleted", false)
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
