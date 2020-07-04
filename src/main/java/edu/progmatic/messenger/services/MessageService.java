package edu.progmatic.messenger.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import edu.progmatic.messenger.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static edu.progmatic.messenger.constans.DateFormats.DATE_TIME_FORMAT_FOR_DATEPICKER;

@Service
public class MessageService {

    @PersistenceContext
    EntityManager em;
    Logger logger = LoggerFactory.getLogger(MessageService.class);


    public List<Message> showMessages(String order, Long limit, String direction, Long topicId, Boolean isDeleted,
                                      String text, String sender, boolean isAdmin, String dateFrom, String dateTo) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        BooleanBuilder whereCondition = new BooleanBuilder();
        QMessage message = QMessage.message;
        QTopic topic = QTopic.topic;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_FOR_DATEPICKER);

        if (topicId != 0) {
            whereCondition.and(message.topic.id.eq(topicId));
        }
        if (!StringUtils.isEmpty(text)) {
            whereCondition.and(message.text.contains(text));
        }
        if (!StringUtils.isEmpty(sender)) {
            whereCondition.and(message.sender.contains(sender));
        }
        if (isDeleted != null && isAdmin) {
            whereCondition.and(message.isDeleted.eq(isDeleted));
        }
        if (dateFrom != null) {
            whereCondition.and(message.dateTime.after(LocalDateTime.parse(dateFrom, formatter)));
        }
        if (dateTo != null) {
            whereCondition.and(message.dateTime.before(LocalDateTime.parse(dateTo, formatter)));
        }

        return queryFactory.selectFrom(message).join(message.topic, topic)
                .where(whereCondition)
                .orderBy(orderSpecifier(direction, orderBySelect(order)))
                .limit(limit)
                .fetch();


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

    public ComparableExpressionBase orderBySelect(String order) {
        if (order.equals("dateTime")) {
            return QMessage.message.dateTime;
        } else if (order.equals("text")) {
            return QMessage.message.text;
        } else if (order.equals("name")) {
            return QMessage.message.topic.name;
        } else {
            return QMessage.message.sender;
        }
    }


    private OrderSpecifier<?> orderSpecifier(String order, ComparableExpressionBase expression) {
        if (order.equals("desc")) {
            return expression.desc();
        } else {
            return expression.asc();

        }
    }

}
