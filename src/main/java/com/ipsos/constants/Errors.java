package com.ipsos.constants;

public enum Errors {
    ;

    public static final String USER_EXISTS = "Username %s already exists.";
    public static final String INVALID_USERNAME = "Invalid username.";
    public static final String INVALID_PASSWORD = "Invalid password.";
    public static final String USERNAME_REQUIRED = "Username required.";
    public static final String PASSWORD_REQUIRED = "Password required.";
    public static final String USERNAME_LENGTH_ERROR = "Username must be between 2 and 20 characters long.";
    public static final String PASSWORD_LENGTH_ERROR = "Password must be between 6 and 20 characters long.";
    public static final String INVALID_USER_DATA = "Invalid user.";
    public static final String PROJECT_NOT_FOUND = "Project not found.";
    public static final String USER_NOT_FOUND = "User not found.";
    public static final String USER_ALREADY_ASSIGNED = "The project is already assigned to this user.";
    public static final String NEW_DATE_MUST_BE_IN_FUTURE = "New due date must be in the future.";
    public static final String PROJECT_NOT_ASSIGNED_TO_USER = "The user does not have this project assigned.";
}
