package ru.javanatnat.purchases.statistics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javanatnat.purchases.request.ReadJsonDataException;
import ru.javanatnat.purchases.request.RequestReader;

import java.io.File;
import java.io.IOException;

public class StatReaderImpl extends RequestReader<StatisticIntervalCriteriaImpl> {
    private static final Logger LOG = LoggerFactory.getLogger(StatReaderImpl.class);
    @Override
    public StatisticIntervalCriteriaImpl read(String fileName) {
        try {
            LOG.info("read request from file: {}", fileName);
            File inputFile = new File(fileName);
            return objectMapper.readValue(inputFile, StatisticIntervalCriteriaImpl.class);
        } catch (IOException e) {
            throw new ReadJsonDataException(e.getMessage(), e);
        }
    }
}
