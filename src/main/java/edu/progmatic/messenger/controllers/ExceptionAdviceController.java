package edu.progmatic.messenger.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.InputMismatchException;

@ControllerAdvice
public class ExceptionAdviceController {
    @ExceptionHandler(Exception.class)
    public String exception(Exception ex, Model model){
        model.addAttribute("exception", ex.getMessage());
        String s = Arrays.toString(ex.getStackTrace());
        model.addAttribute("excStackTrace", s);

        return "exception";
    }

    /*@ExceptionHandler(InputMismatchException.class)
    public String missMatchException(Exception ex, Model model){
        model.addAttribute("missMatchExc", ex);
        return "exception";
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    public String outOfBoundsException(Exception ex, Model model){
        model.addAttribute("outOfBoundsExc", ex);
        return "exception";
    }*/


}


