package ru.javanatnat.purchases.response;

import java.util.Locale;

public enum TypeResponse {
    SEARCH,
    STAT,
    ERROR;

    public String getLowercaseName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
