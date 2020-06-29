package edu.progmatic.messenger.services;

import edu.progmatic.messenger.controllers.MessageController;
import edu.progmatic.messenger.model.Message;
import edu.progmatic.messenger.constans.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static edu.progmatic.messenger.constans.Status.NEM_TOROLT;
import static edu.progmatic.messenger.constans.Status.TOROLT;

@Service
public class MessageService {

    @PersistenceContext
    EntityManager em;

    Logger logger = LoggerFactory.getLogger(MessageController.class);
    private static int idCounter;

    static List<Message> messages= new ArrayList<>();

//    static {
//        messages = new ArrayList<>();
//        messages.addAll(Arrays.asList(new Message("Szia!", "Dezso"), new Message("Csá!", "Jani"),
//                new Message("Szeva!", "Geza"), new Message("Csá!", "Jani"),
//                new Message("Jo napot!", "Kati"), new Message("Udv!", "Laci"),
//                new Message("Csao!", "Robi"), new Message("Szevasz!", "Peti")));
//    }


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
                "SELECT m FROM Message m ORDER BY " + order + " " + direction)
                .setMaxResults(limit)
                .getResultList();
    /*List<Message> results;
    boolean isAsc = isItInAscendingOrder(direction);
    Comparator<Message> comparator = decideOrder(order);
    if (!isAsc) {
        return sortList(comparator.reversed(), limit, model);
    }
    results = sortList(comparator, limit, model);
    return results;*/
        return resultList;
    }

    public List<Message> showNonDeletedMessages(String order, Model model, Integer limit, String direction) {
        //TODO adatbazisos cucc,csak raszurni a deleted oszlopra
        List<Message> results = em.createQuery(
                "SELECT m FROM Message m")
                .setMaxResults(limit)
                .getResultList();

//        List<Message> results;
//        boolean isAsc = isItInAscendingOrder(direction);
//
//        Comparator<Message> comparator = decideOrder(order);
//        if (!isAsc) {
//            return sortListForNonDeleted(comparator.reversed(), limit, model);
//        }
//        results = sortListForNonDeleted(comparator, limit, model);
        return results;
    }

    private List<Message> sortListForNonDeleted(Comparator comp, Integer limit, Model model) {
        Collections.sort(messages, comp);
        List<Message> results = messages.stream().filter(message -> message.getDeleted().equals(NEM_TOROLT))
                .limit(limit).collect(Collectors.toList());
        model.addAttribute("messages", results);
        return results;
    }

    public List<Message> filterByStatus(Status status, List<Message> messages){
        if(status.equals(TOROLT)) {
            return messages.stream()
                    .filter(message -> message.getDeleted().equals(TOROLT)).collect(Collectors.toList());
        } else if(status.equals(NEM_TOROLT)) {
            return messages.stream()
                    .filter(message -> message.getDeleted().equals(NEM_TOROLT)).collect(Collectors.toList());
        }
        return messages;
    }

    public Message showSelectedMessageById(int msgId) {
        return messages.stream()
                .filter(message -> message.getId() == msgId)
                .collect(Collectors.toList()).get(0);
    }
    @Transactional
    public void createNewMessage(Message newMessage) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newMessage.setSender(user.getUsername());
        newMessage.setDateTime(LocalDateTime.now());
        em.persist( newMessage);
        messages.add(newMessage);
    }

    private List<Message> sortList(Comparator comp, int limit, Model model) {
        Collections.sort(messages, comp);
        List<Message> results = messages.stream().limit(limit).collect(Collectors.toList());
        model.addAttribute("messages", results);
        return results;
    }

    //.filter(producer -> producer.getPod().equals(pod))
    public void deleteMessage(int id) {
        //messages.stream().
        messages.stream().filter(message -> message.getId() == id).findFirst().ifPresent(message -> messages.remove(message));
    }

    public void setMessageForDeletion(int id){
        logger.info(id+" ez az ID amit torolni akarunk");
        logger.info(messages.get(id).getId()+ " ez az id a toroltnek");
        messages.stream().forEach(message -> {if (message.getId()==id && message.getDeleted()== NEM_TOROLT){message.setDeleted(TOROLT);}
        else {message.setDeleted(NEM_TOROLT);}});
        //messages.stream().filter(message -> message.getId() == id).findFirst().ifPresent(message -> messages.get(message.getId()).setDeleted(TÖRÖLT));
    }

    /**
     * helper method
     * */
    private Comparator<Message> decideOrder(String order){
        switch (order) {
            case "time":
                return Comparator.comparing(Message::getDateTime);
            case "message":
                return Comparator.comparing(Message::getText);
            case "sender":
            default:
                return Comparator.comparing(Message::getSender);
        }
    }

    /**
     * helper method
     * */
    private boolean isItInAscendingOrder(String direction){
        boolean isAsc = false;
        if (direction.equals("asc")) {
            isAsc = true;
        }
        return isAsc;
    }


}
