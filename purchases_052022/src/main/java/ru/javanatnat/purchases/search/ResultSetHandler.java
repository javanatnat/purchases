package ru.javanatnat.purchases.search;

import java.sql.ResultSet;

@FunctionalInterface
public interface ResultSetHandler<T> {
    T getResult(ResultSet rs);
}
