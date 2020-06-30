package edu.progmatic.messenger.controllers;

import edu.progmatic.messenger.model.Topic;
import edu.progmatic.messenger.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TopicController {

    TopicService topicService;
    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @RequestMapping(value = "/new_topic", method = RequestMethod.POST)
    public String createNewTopic(@ModelAttribute(value = "topic") Topic topic) {
        topicService.createNewTopic(topic.getName());
        return "redirect:/messages";
    }
}
