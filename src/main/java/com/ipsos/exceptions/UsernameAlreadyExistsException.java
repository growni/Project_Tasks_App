package com.ipsos.exceptions;

import static com.ipsos.constants.Messages.USER_EXISTS;

public class UsernameAlreadyExistsException extends RuntimeException{
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
