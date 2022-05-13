package ru.javanatnat.purchases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javanatnat.purchases.criterias.CriteriaProcessorImpl;
import ru.javanatnat.purchases.criterias.CriteriaReaderImpl;
import ru.javanatnat.purchases.datasource.DbExecutor;
import ru.javanatnat.purchases.datasource.DbExecutorImpl;
import ru.javanatnat.purchases.datasource.DriverManagerDataSource;
import ru.javanatnat.purchases.datasource.FlywayMigration;
import ru.javanatnat.purchases.request.TypeRequest;
import ru.javanatnat.purchases.response.ErrorResponse;
import ru.javanatnat.purchases.response.ResponseWriter;
import ru.javanatnat.purchases.search.IllegalSearchCriteriaException;
import ru.javanatnat.purchases.statistics.StatProcessorImpl;
import ru.javanatnat.purchases.statistics.StatReaderImpl;

import javax.sql.DataSource;
import java.util.Arrays;

public final class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);
    private static final AppYml appYml = AppYml.getInstance();

    private final DbExecutor dbExecutor;
    private final TypeRequest type;
    private final String inputFileName;
    private final String outputFileName;
    public App(
            TypeRequest type,
            String inputFileName,
            String outputFileName
    ) {
        this(type, inputFileName, outputFileName, initDb());
    }

    public App(
            TypeRequest type,
            String inputFileName,
            String outputFileName,
            DbExecutor dbExecutor
    ) {
        this.type = type;
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
        this.dbExecutor = dbExecutor;
    }

    public static void main(String[] args) {
        LOG.info("app run with args: {}", Arrays.toString(args));

        if (!(args.length == 3)) {
            throw new RuntimeException("Неверные входные параметры!");
        }

        App app = new App(getType(args[0]), args[1], args[2]);
        app.run();
    }

    public void run() {
        Processor processor = getProcessor();
        new ResponseWriter(getResponse(processor))
                .write(outputFileName);
        LOG.info("success process input file");
    }

    public Response getResponse(Processor processor) {
        try {
            return processor.process(inputFileName);
        } catch (Exception e) {
            LOG.error("error: {} {}", e.getMessage(), e.getCause());
            return ErrorResponse.of(e.getMessage());
        }
    }

    public Processor getProcessor() {
        if (type == TypeRequest.SEARCH) {
            return new CriteriaProcessorImpl(
                    dbExecutor,
                    new CriteriaReaderImpl()
            );
        } else if (type == TypeRequest.STAT) {
            return new StatProcessorImpl(
                    dbExecutor,
                    new StatReaderImpl()
            );
        }

        LOG.error("Неверно задан тип операции: {}", type);
        throw new IllegalSearchCriteriaException("Неверно задан тип операции: " + type);
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
        DataSource dataSource = new DriverManagerDataSource(
                appYml.getUrl(),
                appYml.getUsername(),
                appYml.getPassword());

        FlywayMigration.migrate(dataSource);

        return new DbExecutorImpl(dataSource);
    }
}
