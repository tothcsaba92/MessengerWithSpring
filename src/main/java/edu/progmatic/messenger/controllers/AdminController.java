package edu.progmatic.messenger.controllers;

import edu.progmatic.messenger.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

public class AdminController {
    MessageService messageService;

    @Autowired
    public AdminController(MessageService messageService){
        this.messageService = messageService;
    }

    @PostMapping(value="/messages/{messageId}")
    public String deleteMessage(@PathVariable("messageId") int msgId, Model model) {
        messageService.deleteMessage(msgId);
        model.addAttribute("deletedMessage", msgId);
        return "redirect:/messages";
    }
}
