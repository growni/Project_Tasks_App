package com.ipsos.config;

import com.ipsos.entities.dtos.UserDto;
import com.ipsos.exceptions.InvalidConfirmationPasswordException;
import com.ipsos.exceptions.InvalidPasswordException;
import com.ipsos.exceptions.InvalidUsernameException;
import com.ipsos.exceptions.UsernameAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
    @ExceptionHandler(InvalidUsernameException.class)
    public ModelAndView handleInvalidUsernameException(InvalidUsernameException ex) {
        UserDto userDto = new UserDto();
        ModelAndView view = new ModelAndView("register");

        view.addObject("userDto", userDto);
        view.addObject("usernameError", ex.getMessage());

        return view;
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ModelAndView handleInvalidPasswordException(InvalidPasswordException ex) {
        UserDto userDto = new UserDto();
        ModelAndView view = new ModelAndView("register");

        view.addObject("userDto", userDto);
        view.addObject("passwordError", ex.getMessage());
        return view;
    }

    @ExceptionHandler(InvalidConfirmationPasswordException.class)
    public ModelAndView handleInvalidConfirmationPasswordException(InvalidConfirmationPasswordException ex) {
        UserDto userDto = new UserDto();
        ModelAndView view = new ModelAndView("register");

        view.addObject("userDto", userDto);
        view.addObject("confirmPasswordError", ex.getMessage());
        return view;
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ModelAndView handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        UserDto userDto = new UserDto();
        ModelAndView view = new ModelAndView("register");

        view.addObject("userDto", userDto);
        view.addObject("usernameExistsError", ex.getMessage());
        return view;
    }


}