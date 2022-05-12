package ru.javanatnat.purchases.request;

public class ReadJsonDataException extends RuntimeException {
    public ReadJsonDataException(String message) {
        super(message);
    }

    public ReadJsonDataException(Throwable cause) {
        super(cause);
    }

    public ReadJsonDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
