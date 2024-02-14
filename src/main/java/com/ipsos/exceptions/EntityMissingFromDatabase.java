package com.ipsos.exceptions;

public class EntityMissingFromDatabase extends RuntimeException{
    public EntityMissingFromDatabase(String message) {
        super(message);
    }
}
