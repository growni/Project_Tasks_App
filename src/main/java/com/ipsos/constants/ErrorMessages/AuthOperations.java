package com.ipsos.constants.ErrorMessages;

public enum AuthOperations {
    ;
    public static final String USER_EXISTS = "Username %s already exists.";
    public static final String INVALID_USERNAME = "Invalid username.";
    public static final String INVALID_PASSWORD = "Invalid password.";
    public static final String USERNAME_REQUIRED = "Username required.";
    public static final String PASSWORD_REQUIRED = "Password required.";
    public static final String CONFIRM_PASSWORD_REQUIRED = "Confirmation password required.";
    public static final String CONFIRM_PASSWORD_INCORRECT = "Password and confirmation password not matching.";
    public static final String USERNAME_LENGTH_ERROR = "Username must be between 2 and 20 characters long.";
    public static final String PASSWORD_LENGTH_ERROR = "Password must be between 6 and 20 characters long.";
    public static final String ACTION_NOT_ALLOWED = "You are not allowed to perform this action.";
}
