package edu.progmatic.messenger.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class ExceptionAdviceController {
    Logger logger = LoggerFactory.getLogger(TopicController.class);
    @ExceptionHandler(Exception.class)
    public String exception(Exception ex, Model model) {
        model.addAttribute("exception", ex.getMessage());
        String s = Arrays.toString(ex.getStackTrace());
        model.addAttribute("excStackTrace", s);
        logger.error("error", ex);
       // model.addAttribute("cause", );

        return "exception";
    }

}


