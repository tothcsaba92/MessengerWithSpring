package edu.progmatic.messenger.controllers;

import edu.progmatic.messenger.model.Message;
import edu.progmatic.messenger.model.Topic;
import edu.progmatic.messenger.services.MessageService;
import edu.progmatic.messenger.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
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

    MessageService messageService;
    TopicService topicService;
    @Autowired
    public MessageController(MessageService messageService, TopicService topicService) {
        this.messageService = messageService;
        this.topicService = topicService;
    }


    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public String showMessages(SecurityContextHolderAwareRequestWrapper request,
                               @RequestParam(value = "limit", required = false, defaultValue = Integer.MAX_VALUE + "") long limit,
                               @RequestParam(value = "orderby", required = false, defaultValue = "sender") String order,
                               @RequestParam(value = "direction", required = false, defaultValue = "asc") String direction,
                               Model model) {
        Topic topic = new Topic();
        boolean isAdmin = request.isUserInRole("ROLE_ADMIN");
        List<Message> messages;
        if (isAdmin) {
            messages = messageService.showMessagesForAdmin(order, limit, direction);
        } else {
            messages = messageService.showMessagesForUser(order, limit, direction);
        }
        model.addAttribute("messages", messages);
        model.addAttribute("topic", topic);
        model.addAttribute("topicList", topicService.findAllTopics());

        return "messages";
    }

    @RequestMapping(value = "/messages/{messageId}", method = RequestMethod.GET)
    public String showSelectedMessage(
            @PathVariable("messageId") Long msgId, Model model) {
        Message message = messageService.showSelectedMessageById(msgId);
        if (message != null) {
            model.addAttribute("message", message);
        } else {
            Message nonExistsMessage = new Message(null, null);
            nonExistsMessage.setDateTime(null);
            nonExistsMessage.setId(0);
            model.addAttribute("message", nonExistsMessage);
        }
        return "single_message";
    }

    @GetMapping(value = "/new_message")
    public String showNewMessage(Model model) {
        Message msg = new Message(null, null);
        Topic topic = new Topic();
        msg.setTopic(topic);
        model.addAttribute("newMessage", msg);
        model.addAttribute("topic", topic);
        model.addAttribute("topicList", topicService.findAllTopics());
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
