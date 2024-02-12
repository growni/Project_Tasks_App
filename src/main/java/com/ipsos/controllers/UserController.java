package com.ipsos.controllers;

import com.ipsos.entities.User;
import com.ipsos.entities.dtos.UserDto;
import com.ipsos.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView registerUser(@ModelAttribute UserDto userDto) {
        User newUser = userService.createUser(userDto.getUsername(), userDto.getPassword());

        ModelAndView view = new ModelAndView("dashboard");
        return view;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView showRegisterPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        ModelAndView view = new ModelAndView();
        view.setViewName("register");
        return view;
    }

}
