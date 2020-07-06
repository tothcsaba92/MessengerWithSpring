package edu.progmatic.messenger.controllers;

import edu.progmatic.messenger.dto.MessageDTO;
import edu.progmatic.messenger.dto.TopicDeleteDTO;
import edu.progmatic.messenger.model.Message;
import edu.progmatic.messenger.model.Topic;
import edu.progmatic.messenger.services.MessageService;
import edu.progmatic.messenger.services.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


@Controller
public class MessageController implements WebMvcConfigurer {
    Logger logger = LoggerFactory.getLogger(MessageController.class);
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
                               @RequestParam(value = "topicId", required = false, defaultValue = "0") Long topicId,
                               @RequestParam(value = "isDeleted", required = false, defaultValue = "") Boolean isDeleted,
                               @RequestParam(value = "text", required = false) String text,
                               @RequestParam(value = "sender", required = false) String sender,
                               @RequestParam(value = "dateFrom", required = false) String dateFrom,
                               @RequestParam(value = "dateTo", required = false) String dateTo,
                               Model model) {
        Topic topic = new Topic();
        TopicDeleteDTO topicDeleteDTO = new TopicDeleteDTO();
        boolean isAdmin = request.isUserInRole("ROLE_ADMIN");
        List<Message> messages = messageService.showMessages(order, limit, direction, topicId, isDeleted, text,
                sender, isAdmin, dateFrom, dateTo);
        model.addAttribute("messages", messages);
        model.addAttribute("topic", topic);
        model.addAttribute("topicToDelete", topicDeleteDTO);
        model.addAttribute("topicList", topicService.findAllTopics());
        return "messages";
    }

    @RequestMapping(value = "/messages/{messageId}",method = { RequestMethod.GET, RequestMethod.POST })
    public String showSelectedMessage(@PathVariable("messageId") Long msgId, Model model, HttpServletRequest request,
                                      @ModelAttribute(value = "modifiedText") String modifiedText) {
        Message message = messageService.showSelectedMessageById(msgId);
        if(request.getMethod().equals("POST")){
            messageService.modifyTextOfMessage(message.getId(),modifiedText);
            return "redirect:/messages";
        }
        if (message != null) {
            model.addAttribute("message", message);
        } else {
            Message nonExistsMessage = new Message(null, null,null);
            nonExistsMessage.setDateTime(null);
            nonExistsMessage.setId(0);
            model.addAttribute("message", nonExistsMessage);
        }
        return "single_message";
    }


    @GetMapping(value = "/new_message")
    public String showNewMessage(Model model) {
        MessageDTO newMessage = new MessageDTO();
        Topic topic = new Topic();
        newMessage.setTopic(topic);
        model.addAttribute("newMessage", newMessage);
        model.addAttribute("topic", topic);
        model.addAttribute("topicList", topicService.findAllTopics());
        return "new_message";
    }

    @RequestMapping(value = "/new_message", method = RequestMethod.POST)
    public String createNewMessage(@ModelAttribute(value = "newMessage") @Valid MessageDTO newMessage,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "new_message";
        }
        messageService.createNewMessage(newMessage);
        return "redirect:/messages";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(value = "/messages/deleteTopic")
    public String deleteTopic(@ModelAttribute(value = "topicToDelete") TopicDeleteDTO topicDeleteDTO) {
        topicService.deleteTopicById(topicDeleteDTO.getId());
        return "redirect:/messages";
    }
}
