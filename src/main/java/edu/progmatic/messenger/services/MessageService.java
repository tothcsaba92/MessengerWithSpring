package edu.progmatic.messenger.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import edu.progmatic.messenger.dto.MessageDTO;
import edu.progmatic.messenger.model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


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
        if (StringUtils.isNotBlank(text)) {
            whereCondition.and(message.text.contains(text));
        }
        if (StringUtils.isNotBlank(sender)) {
            whereCondition.and(message.sender.contains(sender));
        }
        if (isDeleted != null && isAdmin) {
            whereCondition.and(message.isDeleted.eq(isDeleted));
        }
        if (StringUtils.isNotBlank(dateFrom)) {
            whereCondition.and(message.dateTime.after(LocalDateTime.parse(dateFrom, formatter)));
        }
        if (StringUtils.isNotBlank(dateTo)) {
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
                .getSingleResult();
    }

    @Transactional
    public void createNewMessage(MessageDTO newMessageDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        User user = (User) principal;
        Topic topic = em.find(Topic.class, newMessageDTO.getTopic().getId());
        Message newMessage = new Message(newMessageDTO.getText(),user.getName(),topic);
        em.persist(newMessage);
    }

    @Transactional
    public void setMessageForDeletion(Long id) {
        Message messageForDeletion = em.find(Message.class, id);
        messageForDeletion.setDeleted(!messageForDeletion.isDeleted());
    }

    @Transactional
    public void modifyTextOfMessage(long id, String text,Integer sleepTime){

        try {
            Message modifiedMessage = em.find(Message.class, id);
            logger.info("alvasido");
            Thread.sleep(sleepTime * 1000);
            logger.info("ebreszto");
            modifiedMessage.setText(text);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }

    }

    private ComparableExpressionBase orderBySelect(String order) {
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
        return order.equals("desc") ? expression.desc() : expression.asc();
    }

}
