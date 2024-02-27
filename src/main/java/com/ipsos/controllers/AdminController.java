package com.ipsos.controllers;


import com.ipsos.entities.User;
import com.ipsos.entities.dtos.UserDto;
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

    @PostMapping("/user_details/{userId}/edit/username")
    public String updateUsername(@RequestParam Long userId, @RequestParam String username) {

        userService.updateUsername(userId, username);

        return "redirect:/admin/user_details/" + userId;
    }

    @PostMapping("/user_details/{userId}/edit/password")
    public String updatePassword(@RequestParam Long userId, @RequestParam String password) {

        userService.updatePassword(userId, password);

        return "redirect:/admin/user_details/" + userId;
    }

    @PostMapping("/user_details/{userId}/add/role")
    public String addRole(@RequestParam Long userId, @RequestParam String roleTypeAdd) {

        userService.addRole(userId, roleTypeAdd);

        return "redirect:/admin/user_details/" + userId;
    }

    @PostMapping("/user_details/{userId}/remove/role")
    public String removeRole(@RequestParam Long userId, @RequestParam String roleTypeRemove) {

        userService.removeRole(userId, roleTypeRemove);

        return "redirect:/admin/user_details/" + userId;
    }

    @PostMapping("/user_details/{userId}/disable")
    public String disableAccount(@RequestParam Long userId) {

        userService.disableAccount(userId);

        return "redirect:/admin/user_details/" + userId;
    }

    @PostMapping("/user_details/{userId}/activate")
    public String activateAccount(@RequestParam Long userId) {

        userService.activateAccount(userId);

        return "redirect:/admin/user_details/" + userId;
    }

}
