package ru.javanatnat.purchases.criterias;

import org.junit.jupiter.api.Test;
import ru.javanatnat.purchases.Response;
import ru.javanatnat.purchases.datasource.DbExecutor;
import ru.javanatnat.purchases.response.CriteriaResponse;
import ru.javanatnat.purchases.response.CriteriaResponseRowResult;
import ru.javanatnat.purchases.response.TypeResponse;
import ru.javanatnat.purchases.search.Buyer;
import ru.javanatnat.purchases.search.ResultSetHandler;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CriteriaProcessorTest {
    private static final CriteriaReaderImpl READER = mock(CriteriaReaderImpl.class);
    private static final DbExecutor DB_EXECUTOR = mock(DbExecutor.class);
    private static final CriteriaProcessorImpl PROCESSOR = new CriteriaProcessorImpl(DB_EXECUTOR, READER);

    private static final String FILEPATH_LAST_NAME_CRITERIA = "test.json";
    private static final String FILEPATH_ALL_CRITERIAS = "test_all.json";
    private static final Criteria LAST_NAME_CRITERIA;
    private static final Criteria MIN_MAX_CRITERIA;
    private static final Criteria BAD_CUSTOMERS_CRITERIA;
    private static final Criteria PRODUCT_MIN_TIMES_CRITERIA;
    private static final List<Buyer> BUYERS;

    static {
        String lastName = "Ivanov";
        String firstName = "Ivan";

        LAST_NAME_CRITERIA = new LastNameCriteriaImpl(lastName);
        MIN_MAX_CRITERIA = new MinMaxExpensesCriteriaImpl(100, 200);
        BAD_CUSTOMERS_CRITERIA = new BadCustomersCriteriaImpl(2);
        PRODUCT_MIN_TIMES_CRITERIA = new ProductMinTimesCriteriaImpl("milk", 2);

        BUYERS = List.of(
                new Buyer(1L, firstName, lastName),
                new Buyer(2L, firstName, lastName));

        when(DB_EXECUTOR.executeSelect(
                anyString(),
                anyList(),
                any(ResultSetHandler.class)))
                .thenReturn(Optional.of(BUYERS));

        given(READER.read(FILEPATH_LAST_NAME_CRITERIA)).willReturn(List.of(LAST_NAME_CRITERIA));
        given(READER.read(FILEPATH_ALL_CRITERIAS))
                .willReturn(
                        List.of(LAST_NAME_CRITERIA,
                                LAST_NAME_CRITERIA,
                                MIN_MAX_CRITERIA,
                                BAD_CUSTOMERS_CRITERIA,
                                PRODUCT_MIN_TIMES_CRITERIA));
    }

    @Test
    public void getResponseLastNameCriteriaTest() {
        Response response = PROCESSOR.process(FILEPATH_LAST_NAME_CRITERIA);
        assertThat(response).isExactlyInstanceOf(CriteriaResponse.class);

        CriteriaResponse criteriaResponse = (CriteriaResponse) response;

        assertThat(criteriaResponse.getType()).isEqualTo(TypeResponse.SEARCH.getLowercaseName());
        assertThat(criteriaResponse.getResults().size()).isEqualTo(1);

        CriteriaResponseRowResult rowResult = criteriaResponse.getResults().get(0);
        assertThat(rowResult.getCriteria()).isEqualTo(LAST_NAME_CRITERIA);
        assertThat(rowResult.getResults()).isEqualTo(BUYERS);
    }

    @Test
    public void getResponseAllCriteriasTest() {
        Response response = PROCESSOR.process(FILEPATH_ALL_CRITERIAS);
        assertThat(response).isExactlyInstanceOf(CriteriaResponse.class);

        CriteriaResponse criteriaResponse = (CriteriaResponse) response;

        assertThat(criteriaResponse.getType()).isEqualTo(TypeResponse.SEARCH.getLowercaseName());
        assertThat(criteriaResponse.getResults().size()).isEqualTo(5);

        List<CriteriaResponseRowResult> criteriaResults = criteriaResponse.getResults();

        assertThat(criteriaResults.get(0).getCriteria()).isEqualTo(LAST_NAME_CRITERIA);
        assertThat(criteriaResults.get(0).getResults()).isEqualTo(BUYERS);

        assertThat(criteriaResults.get(1).getCriteria()).isEqualTo(LAST_NAME_CRITERIA);
        assertThat(criteriaResults.get(1).getResults()).isEqualTo(BUYERS);

        assertThat(criteriaResults.get(2).getCriteria()).isEqualTo(MIN_MAX_CRITERIA);
        assertThat(criteriaResults.get(2).getResults()).isEqualTo(BUYERS);

        assertThat(criteriaResults.get(3).getCriteria()).isEqualTo(BAD_CUSTOMERS_CRITERIA);
        assertThat(criteriaResults.get(3).getResults()).isEqualTo(BUYERS);

        assertThat(criteriaResults.get(4).getCriteria()).isEqualTo(PRODUCT_MIN_TIMES_CRITERIA);
        assertThat(criteriaResults.get(4).getResults()).isEqualTo(BUYERS);
    }
}
