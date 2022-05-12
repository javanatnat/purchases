package ru.javanatnat.purchases.search;

public class ProcessDataException extends RuntimeException {
    public ProcessDataException(Throwable cause) {
        super("Ошибка получения данных", cause);
    }
}
