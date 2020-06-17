package edu.progmatic.messenger.controllers;

import edu.progmatic.messenger.model.Message;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    static List<Message> messageList;

    static {
        messageList = Arrays.asList(new Message("Szia!", "Dezso"), new Message("Csá!", "Jani"),
                new Message("Szeva!", "Geza"), new Message("Csá!", "Jani"),
                new Message("Jo napot!", "Kati"),new Message("Udv!", "Laci"),
                new Message("Csao!", "Robi"), new Message("Szevasz!", "Peti"));
    }

    @RequestMapping(value = "/greeting", method = RequestMethod.GET)
    public String homePage(Model model) {
        model.addAttribute("message", "Hello world!");
        return "home";
    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public String limitedMessages(
            @RequestParam(value = "limit", required = false, defaultValue = "5") int limit ,
            @RequestParam(value = "orderby", required = false, defaultValue = "author") String order,
            @RequestParam(value = "direction", required = false, defaultValue = "desc") String dir, Model model) {
        System.out.println(limit + "ez a limit");

        if (limit <= messageList.size()) {
            List<Message> resultList = switcher(order, model, limit, dir);
            model.addAttribute("messages", resultList);

        } else {
            limit = messageList.size();
            List<Message> resultList = switcher(order, model, limit, dir);
            model.addAttribute("messages", resultList);
        }
        return "message";
    }


//    @RequestMapping(value = "/message", method = RequestMethod.GET)
//    public String messagePage(Model model) {
//
//        model.addAttribute("messages", messageList);
//        return "message";
//    }

   /*@RequestMapping(value = "/addMesssage", method = RequestMethod.GET)
    public String addMessage(
            @RequestParam("text") String text, @RequestParam("author") String author,  Model model) {
        List<Message> productList = productService.findAll(userId)
        model.addAttribute("products", productList);
        return "showproducts";
    }*/


    public List<Message> switcher(String order, Model model, int limit, String direction) {
        boolean isAsc = false;
        List<Message> results = new ArrayList<>();
        if (direction.equals("asc")) {
            isAsc = true;
        }
        switch (order) {
            case "dateTime":
                Comparator<Message> comparator;
                if (isAsc) {
                    comparator = Comparator.comparing(Message::getDateTime);
                } else {
                    comparator = Comparator.comparing(Message::getDateTime).reversed();
                }
                results = sortList(messageList, comparator, limit, model);
                break;
            case "author":
                Comparator<Message> comparator2;
                if (isAsc) {
                    comparator2 = Comparator.comparing(Message::getSender);
                } else {
                    comparator2 = Comparator.comparing(Message::getSender).reversed();
                }
                results = sortList(messageList, comparator2, limit, model);
                break;
            case "message":
                Comparator<Message> comparator3;
                if (isAsc) {
                    comparator3 = Comparator.comparing(Message::getMessage);
                } else {
                    comparator3 = Comparator.comparing(Message::getMessage).reversed();
                }
                results = sortList(messageList, comparator3, limit, model);
                break;
        }
        return results;
    }

    private List<Message> sortList(List<Message> messagesList, Comparator comp, int limit, Model model){
        Collections.sort(messageList, comp);
        List<Message> results = messageList.stream().limit(limit).collect(Collectors.toList());
        model.addAttribute("messages", results);
        return results;
    }
}