package ru.javanatnat.purchases.request;

import java.util.Locale;

public enum TypeRequest {
    SEARCH,
    STAT;

    public static TypeRequest of(String type) {
        String upperType = type.toUpperCase(Locale.ROOT);
        return TypeRequest.valueOf(upperType);
    }
}
