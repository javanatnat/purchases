package ru.javanatnat.purchases.statistics;

import ru.javanatnat.purchases.Processor;
import ru.javanatnat.purchases.Response;
import ru.javanatnat.purchases.datasource.DbExecutor;
import ru.javanatnat.purchases.response.StatResponse;
import ru.javanatnat.purchases.response.TypeResponse;
import ru.javanatnat.purchases.search.IllegalSearchCriteriaException;

public class StatProcessorImpl implements Processor {
    private final DbExecutor dbExecutor;

    public StatProcessorImpl(DbExecutor dbExecutor) {
        this.dbExecutor = dbExecutor;
    }

    @Override
    public Response process(String filename) {
        StatisticIntervalCriteriaImpl statCriteria = getStatCriteria(filename);

        var result = dbExecutor.executeSelect(
                statCriteria.getPreparedSql(),
                statCriteria.getFactParams(),
                StatSqlResultSetProcessor::getResult);

        StatResponse response = new StatResponse(
                TypeResponse.STAT.getLowercaseName(),
                statCriteria.getCountIntervalDays());

        long totalSum = 0;
        int count = 0;

        if (result.isPresent()) {
            var customers = result.get();
            count = customers.size();
            for (var customer : customers) {
                response.addCustomer(customer);
                totalSum += customer.getTotalExpenses();
            }
        }

        double avgSum = 0;
        if (count > 0) {
            avgSum = totalSum/count;
        }

        response.setTotalExpenses(totalSum);
        response.setAvgExpenses(avgSum);

        return response;
    }

    private static StatisticIntervalCriteriaImpl getStatCriteria(String filename) {
        try {
            StatReaderImpl reader = new StatReaderImpl();
            return reader.read(filename);
        } catch (Exception e) {
            throw new IllegalSearchCriteriaException(
                    "Ошибка чтения параметров статистики: " + e.getMessage(), e);
        }
    }
}
