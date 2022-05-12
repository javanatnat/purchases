package ru.javanatnat.purchases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javanatnat.purchases.criterias.CriteriaProcessorImpl;
import ru.javanatnat.purchases.datasource.DbExecutor;
import ru.javanatnat.purchases.datasource.DbExecutorImpl;
import ru.javanatnat.purchases.datasource.DriverManagerDataSource;
import ru.javanatnat.purchases.datasource.FlywayMigration;
import ru.javanatnat.purchases.request.TypeRequest;
import ru.javanatnat.purchases.response.ErrorResponse;
import ru.javanatnat.purchases.response.ResponseWriter;
import ru.javanatnat.purchases.search.IllegalSearchCriteriaException;
import ru.javanatnat.purchases.statistics.StatProcessorImpl;

import javax.sql.DataSource;
import java.util.Arrays;

public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        LOG.info("app run with args: {}", Arrays.toString(args));

        if (!(args.length == 3)) {
            throw new RuntimeException("Неверные входные параметры!");
        }

        TypeRequest type = getType(args[0]);
        String inputFileName = args[1];
        String outputFileName = args[2];

        DbExecutor dbExecutor = initDb();

        try {
            Processor processor = getProcessor(type, dbExecutor);
            Response response = processor.process(inputFileName);
            new ResponseWriter(response).write(outputFileName);
            LOG.info("success process input file");

        } catch (Exception e) {
            LOG.error("error: {} {}", e.getMessage(), e.getCause());
            new ResponseWriter(ErrorResponse.of(e.getMessage())).write(outputFileName);
        }

    }

    private static TypeRequest getType(String value) {
        try {
            TypeRequest type = TypeRequest.of(value);
            LOG.info("тип операции: {}", type);
            return type;

        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new RuntimeException("Неверные входные параметры!");
        }
    }

    private static DbExecutor initDb() {
        AppYml appYml = AppYml.getInstance();
        DataSource dataSource = new DriverManagerDataSource(
                appYml.getUrl(),
                appYml.getUsername(),
                appYml.getPassword());

        FlywayMigration.migrate(dataSource);

        return new DbExecutorImpl(dataSource);
    }

    private static Processor getProcessor(TypeRequest type, DbExecutor dbExecutor) {
        if (type == TypeRequest.SEARCH) {
            return new CriteriaProcessorImpl(dbExecutor);
        } else if (type == TypeRequest.STAT) {
            return new StatProcessorImpl(dbExecutor);
        }
        LOG.error("Неверно задан тип операции: {}", type);
        throw new IllegalSearchCriteriaException("Неверно задан тип операции: " + type);
    }
}
