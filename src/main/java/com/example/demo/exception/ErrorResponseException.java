package com.example.demo.exception;

public class ErrorResponseException extends RuntimeException{
    public ErrorResponseException(String message) {
        super(message);
    }
}
