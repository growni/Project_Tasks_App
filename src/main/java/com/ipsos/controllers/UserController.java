package com.ipsos.controllers;

import com.ipsos.entities.dtos.UserDto;
import com.ipsos.services.UserService;
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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView registerUser(@ModelAttribute UserDto userDto, RedirectAttributes redirectAttributes) {

        ModelAndView view = new ModelAndView("redirect:/register");

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

}
