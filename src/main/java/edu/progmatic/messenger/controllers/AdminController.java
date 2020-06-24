package edu.progmatic.messenger.controllers;

import edu.progmatic.messenger.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {
    MessageService messageService;

    @Autowired
    public AdminController(MessageService messageService){
        this.messageService = messageService;
    }
    /*@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value="/messages/delete/{messageId}")
    public String deleteMessage(@PathVariable("messageId") int msgId) {
        messageService.deleteMessage(msgId);
        return "redirect:/messages";
    }*/

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value="/messages/delete/{messageId}")
    public String showAllMessage(@PathVariable("messageId") int msgId) {
        messageService.setMessageForDeletion(msgId);
        return "redirect:/messages";
    }

}
