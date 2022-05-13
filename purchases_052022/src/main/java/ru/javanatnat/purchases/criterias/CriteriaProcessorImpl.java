package ru.javanatnat.purchases.criterias;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javanatnat.purchases.Processor;
import ru.javanatnat.purchases.Response;
import ru.javanatnat.purchases.datasource.DbExecutor;
import ru.javanatnat.purchases.response.CriteriaResponse;
import ru.javanatnat.purchases.response.CriteriaResponseRowResult;
import ru.javanatnat.purchases.response.TypeResponse;
import ru.javanatnat.purchases.search.IllegalSearchCriteriaException;

import java.util.List;

public class CriteriaProcessorImpl implements Processor {
    private static final Logger LOG = LoggerFactory.getLogger(CriteriaProcessorImpl.class);

    private final DbExecutor dbExecutor;
    private final CriteriaReaderImpl reader;

    public CriteriaProcessorImpl(
            DbExecutor dbExecutor,
            CriteriaReaderImpl reader
    ) {
        this.dbExecutor = dbExecutor;
        this.reader = reader;
    }

    @Override
    public Response process(String fileName) {
        List<Criteria> criterias = getCriterias(fileName);

        CriteriaResponse response = new CriteriaResponse(
                TypeResponse.SEARCH.getLowercaseName());

        for (var criteria : criterias) {
            LOG.info("process criteria: {}", criteria);
            var result = dbExecutor.executeSelect(
                    criteria.getPreparedSql(),
                    criteria.getFactParams(),
                    CriteriaSqlResultSetProcessor::getResult);

            if (result.isPresent()) {
                LOG.info("criteria results count: {}", result.get().size());
            } else {
                LOG.info("criteria results count: 0");
            }

            CriteriaResponseRowResult rowResult = new CriteriaResponseRowResult(criteria);
            result.ifPresent(rowResult::addBuyers);

            response.addRowResult(rowResult);
        }

        return response;
    }

    private List<Criteria> getCriterias(String filename) {
        try {
            return reader.read(filename);
        } catch (Exception e) {
            throw new IllegalSearchCriteriaException(
                    "Ошибка чтения параметров поиска по критериям: " + e.getMessage(), e);
        }
    }
}
