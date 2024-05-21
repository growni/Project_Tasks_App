package com.ipsos.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class LoginController {
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView signInPage(@RequestParam(value = "error", required = false) String error) {
        ModelAndView view = new ModelAndView("login");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        System.out.println(authentication);

        if(!username.equals("anonymousUser")) {
            view.setViewName("redirect:/dashboard");
            return view;
        }

        if(error != null) {
            view.addObject("error", "Invalid username or password");
        }

        return view;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(@RequestParam String username, @RequestParam String password) {
        ModelAndView view = new ModelAndView("redirect:/dashboard");

        return view;
    }
}
