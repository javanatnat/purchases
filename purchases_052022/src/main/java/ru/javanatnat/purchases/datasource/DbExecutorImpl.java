package ru.javanatnat.purchases.datasource;

import ru.javanatnat.purchases.search.ProcessDataException;
import ru.javanatnat.purchases.search.ResultSetHandler;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DbExecutorImpl implements DbExecutor {
    private final DataSource dataSource;

    public DbExecutorImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public <T> Optional<T> executeSelect(
            String sql,
            List<Object> params,
            ResultSetHandler<T> rsHandler
    ) {
        try (var pst = dataSource.getConnection().prepareStatement(sql)) {
            int idx = 0;
            for(Object param : params) {
                idx += 1;
                pst.setObject(idx, param);
            }

            try (var rs = pst.executeQuery()) {
                var result = rsHandler.getResult(rs);
                return Optional.ofNullable(result);
            }
        } catch (SQLException e) {
            throw new ProcessDataException(e);
        }
    }
}
