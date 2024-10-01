package org.example.task5.exception;

public class CategoryNotExistException extends RuntimeException {

    public CategoryNotExistException(String message) {
        super(message);
    }
}
