package com.ipsos.constants;

public enum Regex {
    ;
    public static final String USERNAME_REGEX = "^(?!\\s*$)[\\p{L}\\d'\\-.]{2,50}$";
    public static final String PASSWORD_REGEX = "^(?!.*\\s).{6,20}$";
    public static final String NAME_REGEX = "^(?!\\s*$).{2,50}$";
}
