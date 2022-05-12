package ru.javanatnat.purchases.datasource;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public class FlywayMigration {

    private static final Logger LOG = LoggerFactory.getLogger(FlywayMigration.class);

    public static void migrate(DataSource dataSource) {
        LOG.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .load();
        flyway.migrate();
        LOG.info("db migration finished.");
    }
}
