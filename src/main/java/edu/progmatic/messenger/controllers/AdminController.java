package edu.progmatic.messenger.controllers;

import edu.progmatic.messenger.model.User;
import edu.progmatic.messenger.services.MessageService;
import edu.progmatic.messenger.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class AdminController {
    MessageService messageService;
    UserService userService;



    @Autowired
    public AdminController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }
    /*@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value="/messages/delete/{messageId}")
    public String deleteMessage(@PathVariable("messageId") int msgId) {
        messageService.deleteMessage(msgId);
        return "redirect:/messages";
    }*/

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/messages/delete/{messageId}")
    public String showAllMessage(@PathVariable("messageId") int msgId) {
        messageService.setMessageForDeletion(msgId);
        return "redirect:/messages";
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/members")
    public String viewMembers(Model model) {
        List<User> list = userService.getUsers();
        model.addAttribute("members", list);
        return "members";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/members/promote/{username}")
    public String promoteToAdmin(@PathVariable("username") String username) {
        userService.promote(username);
        return "redirect:/members";
    }

}
