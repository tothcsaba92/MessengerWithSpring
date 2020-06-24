package edu.progmatic.messenger.controllers;

import edu.progmatic.messenger.model.Message;
import edu.progmatic.messenger.services.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Valid;
import java.util.List;


@Controller
public class MessageController implements WebMvcConfigurer {

    Logger logger = LoggerFactory.getLogger(MessageController.class);
    MessageService messageService = new MessageService();


    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public String limitedMessages(SecurityContextHolderAwareRequestWrapper request,
                                  @RequestParam(value = "limit", required = false, defaultValue = Integer.MAX_VALUE + "") int limit,
                                  @RequestParam(value = "orderby", required = false, defaultValue = "sender") String order,
                                  @RequestParam(value = "direction", required = false, defaultValue = "asc") String dir, Model model) {
        logger.info("eler e ide?");
        boolean isAdmin = request.isUserInRole("ROLE_ADMIN");
        if (isAdmin) {
            List<Message> messages = messageService.showMessages(order, model, limit, dir);
            model.addAttribute("messages", messages);
            logger.info("recognized as admin");
        } else {
            List<Message> messages = messageService.showNonDeletedMessages(order, model, limit, dir);
            model.addAttribute("messages", messages);
            logger.info("recognized as user");
        }

        return "messages";
    }

    @RequestMapping(value = "/messages/{messageId}", method = RequestMethod.GET)
    public String showSelectedMessageId(
            @PathVariable("messageId") int msgId, Model model) {
        Message message = messageService.showSelectedMessageById(msgId);
        if (message != null) {
            model.addAttribute("message", message);
        } else {
            Message nonExistMessage = new Message(null, null);
            nonExistMessage.setDateTime(null);
            nonExistMessage.setId(0);
            model.addAttribute("message", nonExistMessage);
        }
        return "single_message";
    }


    @GetMapping(value = "/new_message")
    public String showNewMessage(Model model) {
        model.addAttribute("newMessage", new Message(null, null));
        return "new_message";
    }

    @RequestMapping(value = "/new_message", method = RequestMethod.POST)
    public String createNewMessage(@ModelAttribute(value = "newMessage") @Valid Message newMessage,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/new_message";
        }
        messageService.createNewMessage(newMessage);
        return "redirect:/messages";
    }
}
