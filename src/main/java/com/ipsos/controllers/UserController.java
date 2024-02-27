package com.ipsos.controllers;

import com.ipsos.entities.dtos.UserDto;
import com.ipsos.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.ipsos.constants.ErrorMessages.AuthOperations.CONFIRM_PASSWORD_INCORRECT;


@RestController
@RequestMapping("/")
public class UserController {

    private final UserService userService;
    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView registerUser(@ModelAttribute UserDto userDto, RedirectAttributes redirectAttributes) {

        ModelAndView view = new ModelAndView("redirect:/register");

        System.out.println(userDto);

        if(!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("errorMessage", CONFIRM_PASSWORD_INCORRECT);

            return view;
        }

        userService.registerUser(userDto);

        view.setViewName("redirect:/dashboard");
        return view;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView showRegisterPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        ModelAndView view = new ModelAndView();
        view.setViewName("register");
        return view;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutSuccess() {
        printUserRoles();
        return "logout-success";
    }

    public void printUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("Username: " + authentication.getName());
            authentication.getAuthorities().forEach(authority -> {
                System.out.println("User has role: " + authority.getAuthority());
            });
        }
    }

}
