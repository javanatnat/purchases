package ru.javanatnat.purchases.search;

public class IllegalSearchCriteriaException extends RuntimeException {
    public IllegalSearchCriteriaException(String message) {
        super(message);
    }

    public IllegalSearchCriteriaException(String message, Throwable cause) {
        super(message, cause);
    }
}
