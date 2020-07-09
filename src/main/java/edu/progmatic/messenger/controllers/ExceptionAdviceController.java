package edu.progmatic.messenger.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class ExceptionAdviceController {
    Logger logger = LoggerFactory.getLogger(ExceptionAdviceController.class);

    @ExceptionHandler(Exception.class)
    public String exception(Exception ex, Model model) {
        model.addAttribute("exception", ex);
        logger.error(ex.getMessage());
        logger.error(Arrays.toString(ex.getStackTrace()));

        String stackTraceText = Arrays.toString(ex.getStackTrace());
        model.addAttribute("excStackTrace", stackTraceText);
        logger.error("error", ex);
        return "exception";
    }
}