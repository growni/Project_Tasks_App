package com.ipsos.constants;

public enum Regex {
    ;
    public static final String USERNAME_REGEX = "^[a-zA-Z0-9._-]{3,15}$";
    public static final String PASSWORD_REGEX = "^(?!.*\\s).{6,20}$";
}
