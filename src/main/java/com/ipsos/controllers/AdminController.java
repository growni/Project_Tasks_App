package com.ipsos.controllers;


import com.ipsos.entities.User;
import com.ipsos.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @RequestMapping(method = RequestMethod.GET)
    public String getUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "/admin";
    }

    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.GET)
    public ModelAndView showDeleteConfirmation(@PathVariable Long userId) {
        ModelAndView view = new ModelAndView("delete_confirmation");
        view.addObject("userId", userId);
        System.out.println("User id from GET: "+userId);
        return view;
    }

    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.POST)
    public ModelAndView deleteUser(@RequestParam Long userId) {
        System.out.println("user id: " + userId);
        this.userService.deleteUser(userId);

        ModelAndView view = new ModelAndView("redirect:/admin");
        return view;
    }
}
