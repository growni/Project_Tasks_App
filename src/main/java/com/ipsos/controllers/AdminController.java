package com.ipsos.controllers;


import com.ipsos.entities.User;
import com.ipsos.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String getUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);

        return "/admin";
    }

    @GetMapping("/user_details/{userId}")
    public String getUserDetails(@PathVariable Long userId, Model model) {
        User user = userService.getById(userId);
        model.addAttribute("user", user);

        return "user_details";
    }

    @GetMapping(value = "/user_details/{userId}/delete")
    public ModelAndView showDeleteConfirmation(@PathVariable Long userId) {

        ModelAndView view = new ModelAndView("delete_confirmation");
        view.addObject("userId", userId);

        return view;
    }

    @RequestMapping(value ="/user_details/{userId}/delete", method = RequestMethod.POST)
    public String deleteUser(@PathVariable Long userId, Model model) {

        User user = this.userService.getById(userId);
        model.addAttribute("user", user);

        this.userService.deleteUser(userId);

        return "redirect:/admin";
    }

}
