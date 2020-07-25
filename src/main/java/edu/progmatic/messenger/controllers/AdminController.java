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

/**
 *
 * @author csaba
 */

@Controller
public class AdminController {
    MessageService messageService;
    UserService userService;


    @Autowired
    public AdminController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }
    /**
     * Change the status of the selected messsage to "removed".
     *
     * @param msgId
     *            -- ID of the message.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/messages/delete/{messageId}")
    public String setMessageForDeletion(@PathVariable("messageId") Long msgId) {
       messageService.setMessageForDeletion(msgId);
        return "redirect:/messages";
    }

    /**
     * Show the list of users where status is "ROLE_USER".
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/members")
    public String viewMembers(Model model) {
        List<User> list = userService.findNonAdminUsers();
        model.addAttribute("members", list);
        return "members";
    }

    /**
     * Change the status of selected user for "ROLE_ADMIN".
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/members/promote/{username}")
    public String promoteToAdmin(@PathVariable("username") String username) {
        userService.promoteToAdmin(username);
        return "redirect:/members";
    }

}
