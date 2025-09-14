package org.example.ssfd_ch4_ex3payments.exception;

public class InvalidHashException extends RuntimeException {
    public InvalidHashException(String message) {
        super(message);
    }
}
