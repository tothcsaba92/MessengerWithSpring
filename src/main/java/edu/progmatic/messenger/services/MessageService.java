package edu.progmatic.messenger.services;

import edu.progmatic.messenger.model.Message;
import edu.progmatic.messenger.model.Status;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;
import java.util.stream.Collectors;

import static edu.progmatic.messenger.model.Status.NEM_TÖRÖLT;
import static edu.progmatic.messenger.model.Status.TÖRÖLT;

@Service
public class MessageService {

    static List<Message> messages;

    static {
        messages = new ArrayList<>();
        messages.addAll(Arrays.asList(new Message("Szia!", "Dezso"), new Message("Csá!", "Jani"),
                new Message("Szeva!", "Geza"), new Message("Csá!", "Jani"),
                new Message("Jo napot!", "Kati"), new Message("Udv!", "Laci"),
                new Message("Csao!", "Robi"), new Message("Szevasz!", "Peti")));
    }


    public List<Message> showMessages(String order, Model model, int limit, String direction) {
        boolean isAsc = false;
        List<Message> results;
        if (direction.equals("asc")) {
            isAsc = true;
        }
        Comparator<Message> comparator;
        switch (order) {
            case "time":
                comparator = Comparator.comparing(Message::getDateTime);
                break;
            case "message":
                comparator = Comparator.comparing(Message::getText);
                break;
            case "sender":
            default:
                comparator = Comparator.comparing(Message::getSender);
                break;
        }
        if (!isAsc) {
            return sortList(comparator.reversed(), limit, model);
        }
        results = sortList(comparator, limit, model);
        return results;
    }

    public List<Message> showNonDeletedMessages(String order, Model model, int limit, String direction) {
        boolean isAsc = false;
        List<Message> results;
        if (direction.equals("asc")) {
            isAsc = true;
        }
        Comparator<Message> comparator;
        switch (order) {
            case "time":
                comparator = Comparator.comparing(Message::getDateTime);
                break;
            case "message":
                comparator = Comparator.comparing(Message::getText);
                break;
            case "sender":
            default:
                comparator = Comparator.comparing(Message::getSender);
                break;
        }
        if (!isAsc) {
            return sortListForNonDeleted(comparator.reversed(), limit, model);
        }
        results = sortListForNonDeleted(comparator, limit, model);
        return results;
    }

    private List<Message> sortListForNonDeleted(Comparator comp, int limit, Model model) {
        Collections.sort(messages, comp);
        List<Message> results = messages.stream().filter(message -> message.getDeleted().equals(NEM_TÖRÖLT))
                .limit(limit).collect(Collectors.toList());
        model.addAttribute("messages", results);
        return results;
    }

    public Message showSelectedMessageById(int msgId) {
        return messages.stream()
                .filter(message -> message.getId() == msgId)
                .collect(Collectors.toList()).get(0);
    }

    public void createNewMessage(Message newMessage) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newMessage.setSender(user.getUsername());
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
        messages.stream().filter(message -> message.getId() == id).findFirst().ifPresent(message -> messages.get(message.getId()).setDeleted(TÖRÖLT));
    }


}
