package com.morzevichka.manageyourstore.exceptions;

public class UserBannedException extends RuntimeException {
    public UserBannedException() {
        super("User is banned");
    }

    public UserBannedException(String message) {
        super(message);
    }
}
