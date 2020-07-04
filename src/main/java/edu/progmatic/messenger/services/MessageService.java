package edu.progmatic.messenger.services;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import edu.progmatic.messenger.model.Message;
import edu.progmatic.messenger.model.QMessage;
import edu.progmatic.messenger.model.Topic;
import edu.progmatic.messenger.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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


    public List<Message> showMessagesForAdmin(String order, Long limit, String direction, Long topicId, boolean isDeleted) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        if(topicId != null){
            return queryFactory.selectFrom(QMessage.message)
                    .where(QMessage.message.topic.id.eq(topicId), QMessage.message.isDeleted.eq(isDeleted))
                    .orderBy(orderSpecifier(direction,orderBySelect(order)))
                    .limit(limit)
                    .fetch();
        } else{
            return queryFactory.selectFrom(QMessage.message)
                    .where(QMessage.message.isDeleted.eq(isDeleted))
                    .orderBy(orderSpecifier(direction,orderBySelect(order)))
                    .limit(limit)
                    .fetch();
        }
    }

    public List<Message> showMessagesForUser(String order, Long limit, String direction, Long topicId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        if(topicId != null){
            return queryFactory.selectFrom(QMessage.message)
                    .where(QMessage.message.topic.id.eq(topicId), QMessage.message.isDeleted.eq(false))
                    .orderBy(orderSpecifier(direction,orderBySelect(order)))
                    .limit(limit)
                    .fetch();
        } else{
            return queryFactory.selectFrom(QMessage.message)
                    .where(QMessage.message.isDeleted.eq(false))
                    .orderBy(orderSpecifier(direction,orderBySelect(order)))
                    .limit(limit)
                    .fetch();
        }
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        User user = (User) principal;
        newMessage.setSender(user.getName());
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
        if (order.equals("dateTime")) {
            order = "dateTime";
        } else if (order.equals("text")) {
            order = "text";
        } else {
            order = "sender";
        }
        return order;
    }


    private OrderSpecifier<?> orderSpecifier(String order, String fieldName){
        Path<Object> fieldPath = Expressions.path(Object.class, QMessage.message, fieldName);
        if(order.equals("desc")){
            return new OrderSpecifier(Order.DESC, fieldPath);
        }
        else{
            return new OrderSpecifier(Order.ASC, fieldPath);

        }
    }

}
