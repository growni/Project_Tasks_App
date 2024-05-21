package com.ipsos.controllers;

import com.ipsos.entities.Role;
import com.ipsos.entities.User;
import com.ipsos.entities.dtos.UserDto;
import com.ipsos.exceptions.InvalidDataException;
import com.ipsos.services.Impl.SecurityUserDetails;
import com.ipsos.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

import static com.ipsos.constants.ErrorMessages.AuthOperations.CONFIRM_PASSWORD_INCORRECT;


@RestController
@RequestMapping("/")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final SecurityUserDetails userDetails;
    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    public UserController(UserService userService, AuthenticationManager authenticationManager, SecurityUserDetails userDetails) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetails = userDetails;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView registerUser(@ModelAttribute UserDto userDto, HttpServletRequest request) {

        userService.registerUser(userDto);
        authenticateUserAfterRegistration(request, userDto.getUsername(), userDto.getConfirmPassword());
        ModelAndView view = new ModelAndView("redirect:/dashboard");

        return view;
    }

    public void authenticateUserAfterRegistration(HttpServletRequest request, String username, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManager.authenticate(authToken);
        authToken.setDetails(new WebAuthenticationDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
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

    @GetMapping("/profile")
    public ModelAndView userProfileView(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        model.addAttribute("username", username);

        User user = this.userService.getByUsername(username);

        List<String> userRoleTypes = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

        model.addAttribute("roles", userRoleTypes);

        ModelAndView view = new ModelAndView();
        view.setViewName("profile");

        return view;
    }

    @GetMapping("/profile/change-password")
    public ModelAndView changePasswordView() {
        ModelAndView view = new ModelAndView();

        view.setViewName("change-password");

        return view;
    }

    @PostMapping("/profile/change-password")
    public ModelAndView changePassword(@RequestParam String currentPassword, @RequestParam String newPassword, @RequestParam String confirmPassword) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

//        if(!newPassword.equals(confirmPassword)) {
//            throw new InvalidDataException(CONFIRM_PASSWORD_INCORRECT);
//        }

        Long userId = this.userService.getByUsername(username).getId();

        this.userService.updatePassword(userId, currentPassword, newPassword);

        ModelAndView view = new ModelAndView();
        view.setViewName("redirect:/profile");

        return view;
    }

    @PostMapping("/profile/changeUsername")
    public ModelAndView changeUsername(@RequestParam String newUsername) {

        Authentication oldAuthentication = SecurityContextHolder.getContext().getAuthentication();
        String oldUsername = oldAuthentication.getName();

        Long userId = this.userService.getByUsername(oldUsername).getId();

        System.out.println("New Username = " + newUsername);
        this.userService.updateUsername(userId, newUsername);
        User updatedUser = userService.getByUsername(newUsername);

        Authentication newAuth = new UsernamePasswordAuthenticationToken(updatedUser, oldAuthentication.getCredentials(), oldAuthentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        ModelAndView view = new ModelAndView();
        view.setViewName("redirect:/profile");
        return view;
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
