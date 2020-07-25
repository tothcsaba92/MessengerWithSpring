package edu.progmatic.messenger.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *
 * @author csaba
 */

@Controller
public class  ErrorPageController implements ErrorController {
    private static final String ERR_PATH = "/error";

    private ErrorAttributes errorAttributes;

    @Autowired
    public ErrorPageController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    /**
     * Catches all types of errors to be able to display on the view.
     *
     */
    @GetMapping(ERR_PATH)
    public String error(Model model, HttpServletRequest request) {
        ServletWebRequest requestAttributes = new ServletWebRequest(request);
        Map<String, Object> error = this.errorAttributes.getErrorAttributes(requestAttributes, true);

        model.addAttribute("timestamp", error.get("timestamp"));
        model.addAttribute("error", error.get("error"));
        model.addAttribute("message", error.get("message"));
        model.addAttribute("path", error.get("path"));
        model.addAttribute("status", error.get("status"));

        Object status = error.get("status");
        String code = status.toString();
        if(code.equals("404")){
            return "404";
        }
        if(code.equals("500")){
            return "500";
        }
        else{
            return "detailedError";
        }
    }

    @Override
    public String getErrorPath() {
        return ERR_PATH;
    }
}
