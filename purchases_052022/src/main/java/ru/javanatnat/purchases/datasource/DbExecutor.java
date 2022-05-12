package ru.javanatnat.purchases.datasource;

import ru.javanatnat.purchases.search.ResultSetHandler;

import java.util.List;
import java.util.Optional;

public interface DbExecutor {
    <T> Optional<T> executeSelect(
            String sql,
            List<Object> params,
            ResultSetHandler<T> rsHandler);
}
