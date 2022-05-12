package ru.javanatnat.purchases.criterias;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javanatnat.purchases.request.ReadJsonDataException;
import ru.javanatnat.purchases.request.RequestReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CriteriaReaderImpl extends RequestReader<List<Criteria>> {
    private static final Logger LOG = LoggerFactory.getLogger(CriteriaReaderImpl.class);
    private static final String NO_CRITERIA_ERROR = "не найден подходящий критерий для поиска";
    private static final String CRITERIAS = "criterias";
    private static final String LASTNAME = "lastName";
    private static final String PRODUCT_NAME = "productName";
    private static final String MIN_TIMES = "minTimes";
    private static final String MIN_EXPENSES = "minExpenses";
    private static final String MAX_EXPENSES = "maxExpenses";
    private static final String BAD_CUSTOMERS = "badCustomers";
    @Override
    public List<Criteria> read(String fileName){
        LOG.info("read request from file: {}", fileName);
        File inputFile = new File(fileName);
        JsonNode tree;
        try {
            tree = objectMapper.readTree(inputFile);
        } catch (IOException e) {
            throw new ReadJsonDataException("Невозможно прочитать файл в формате json, формат файла нарушен", e);
        }
        JsonNode criterias = tree.get(CRITERIAS);

        List<Criteria> result = new ArrayList<>();

        if (criterias.isArray()) {
            for (JsonNode criteria : criterias) {
                if (hasNode(criteria, LASTNAME)) {
                    result.add(new LastNameCriteriaImpl(getStringValue(criteria, LASTNAME)));
                }
                if (hasNode(criteria, PRODUCT_NAME) && hasNode(criteria, MIN_TIMES)) {
                    result.add(new ProductMinTimesCriteriaImpl(
                            getStringValue(criteria, PRODUCT_NAME),
                            getIntValue(criteria, MIN_TIMES)));
                }
                if (hasNode(criteria, MIN_EXPENSES) && hasNode(criteria, MAX_EXPENSES)) {
                    result.add(new MinMaxExpensesCriteriaImpl(
                            getIntValue(criteria, MIN_EXPENSES),
                            getIntValue(criteria, MAX_EXPENSES)));
                }
                if (hasNode(criteria, BAD_CUSTOMERS)) {
                    result.add(new BadCustomersCriteriaImpl(getIntValue(criteria, BAD_CUSTOMERS)));
                }
            }
        }

        if (result.isEmpty()) {
            LOG.error("parse file error: {}", NO_CRITERIA_ERROR);
            throw new ReadJsonDataException(NO_CRITERIA_ERROR);
        }

        return result;
    }

    private static boolean hasNode(JsonNode criteria, String field) {
        JsonNode lastName = criteria.get(field);
        return !(lastName == null || lastName.isNull());
    }

    private static String getStringValue(JsonNode criteria, String field) {
        return criteria.get(field).asText();
    }

    private static int getIntValue(JsonNode criteria, String field) {
        return criteria.get(field).asInt();
    }
}
