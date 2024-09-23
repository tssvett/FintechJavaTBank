package org.example.task5.exception;

public class LocationNotExistException extends RuntimeException {

    public LocationNotExistException(String message) {
        super(message);
    }
}
