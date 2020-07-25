package edu.progmatic.messenger.controllers;

import edu.progmatic.messenger.model.Topic;
import edu.progmatic.messenger.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author csaba
 */

@Controller
public class TopicController {
    TopicService topicService;

    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    /**
     * Create new topic, inside a topic we can store messages, its work like a parent-children relation.
     *
     * @param newTopic -- DTO for the creation of new topic, cause not all of the fields required by the user.
     */
    @RequestMapping(value = "/new_topic", method = RequestMethod.POST)
    public String createNewTopic(@ModelAttribute(value = "topic") Topic newTopic) {
        topicService.createNewTopic(newTopic.getName());
        return "redirect:/messages";
    }

}
