package edu.progmatic.messenger.controllers;

import edu.progmatic.messenger.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MessageController implements WebMvcConfigurer {

    static List<Message> messageList;


    static {
        messageList = new ArrayList<>();
        messageList.addAll(Arrays.asList(new Message("Szia!", "Dezso"), new Message("Csá!", "Jani"),
                new Message("Szeva!", "Geza"), new Message("Csá!", "Jani"),
                new Message("Jo napot!", "Kati"), new Message("Udv!", "Laci"),
                new Message("Csao!", "Robi"), new Message("Szevasz!", "Peti")));

    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public String limitedMessages(
            @RequestParam(value = "limit", required = false, defaultValue = Integer.MAX_VALUE +"") int limit,
            @RequestParam(value = "orderby", required = false, defaultValue = "author") String order,
            @RequestParam(value = "direction", required = false, defaultValue = "asc") String dir, Model model) {

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


    @GetMapping(value = "/new_message")
    public String showNewMessage(Model model){
        model.addAttribute("newMessage", new Message(null,null));
        return "new_message";
    }

    @RequestMapping(value = "/new_message", method = RequestMethod.POST)
    public String createNewMessage(@ModelAttribute(value ="newMessage") @Valid Message newMessage,
                                   BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "/new_message";
        }
        messageList.add(newMessage);
        return "redirect:/messages";
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
           return sortList(comparator.reversed(), limit, model) ;
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
