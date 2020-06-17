package edu.progmatic.messenger.controllers;

import edu.progmatic.messenger.model.Message;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MessageController {

    static List<Message> messageList;

    static {
        messageList = Arrays.asList(new Message("Szia!", "Dezso"), new Message("Csá!", "Jani"),
                new Message("Szeva!", "Geza"), new Message("Csá!", "Jani"),
                new Message("Jo napot!", "Kati"), new Message("Udv!", "Laci"),
                new Message("Csao!", "Robi"), new Message("Szevasz!", "Peti"));
    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public String limitedMessages(
            @RequestParam(value = "limit", required = false, defaultValue = "5") int limit,
            @RequestParam(value = "orderby", required = false, defaultValue = "author") String order,
            @RequestParam(value = "direction", required = false, defaultValue = "desc") String dir, Model model) {

        if (limit <= messageList.size()) {
            List<Message> resultList = switcher(order, model, limit, dir);
            model.addAttribute("messages", resultList);

        } else {
            limit = messageList.size();
            List<Message> resultList = switcher(order, model, limit, dir);
            model.addAttribute("messages", resultList);
        }
        return "messages";
    }
    @RequestMapping(value="/messages/{messageId}", method=RequestMethod.GET)
    public String showSelectedMessageId(
            @PathVariable("messageId") int userId, Model model) {
        List<Message> result = messageList.stream()
                .filter(message -> message.getId() == userId)
                .collect(Collectors.toList());
        if (!result.isEmpty()) {
            model.addAttribute("message",result.get(0));
        }
        return "single_message";
    }



    public List<Message> switcher(String order, Model model, int limit, String direction) {
        boolean isAsc = false;
        List<Message> results;
        if (direction.equals("asc")) {
            isAsc = true;
        }
        Comparator<Message> comparator;
        switch (order) {
            case "dateTime":
                comparator = Comparator.comparing(Message::getDateTime);
                break;
            case "message":
                comparator = Comparator.comparing(Message::getText);
                break;
            case "author":
            default:
                comparator = Comparator.comparing(Message::getSender);
                break;
        }
        if (!isAsc) {
            comparator.reversed();
        }
        results = sortList(comparator, limit, model);
        return results;
    }

    private List<Message> sortList(Comparator comp, int limit, Model model) {
        Collections.sort(messageList, comp);
        List<Message> results = messageList.stream().limit(limit).collect(Collectors.toList());
        model.addAttribute("messages", results);
        return results;
    }
}
