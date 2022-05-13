package ru.javanatnat.purchases;

import org.junit.jupiter.api.Test;
import ru.javanatnat.purchases.criterias.CriteriaProcessorImpl;
import ru.javanatnat.purchases.criterias.CriteriaReaderImpl;
import ru.javanatnat.purchases.criterias.LastNameCriteriaImpl;
import ru.javanatnat.purchases.datasource.DbExecutor;
import ru.javanatnat.purchases.request.TypeRequest;
import ru.javanatnat.purchases.response.CriteriaResponse;
import ru.javanatnat.purchases.response.CriteriaResponseRowResult;
import ru.javanatnat.purchases.response.ErrorResponse;
import ru.javanatnat.purchases.response.TypeResponse;
import ru.javanatnat.purchases.search.IllegalSearchCriteriaException;
import ru.javanatnat.purchases.search.ResultSetHandler;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AppTest {
    private static final String LAST_NAME = "Ivanov";
    private static final LastNameCriteriaImpl CRITERIA = new LastNameCriteriaImpl(LAST_NAME);
    private static final CriteriaReaderImpl READER = mock(CriteriaReaderImpl.class);
    private static final DbExecutor DB_EXECUTOR = mock(DbExecutor.class);
    private static final Processor PROCESSOR = new CriteriaProcessorImpl(DB_EXECUTOR, READER);
    private static final TypeRequest TYPE_REQUEST = TypeRequest.SEARCH;
    private static final String INPUT_ERR_FILE_NAME = "inputErr.json";
    private static final String INPUT_FILE_NAME = "input.json";
    private static final String OUTPUT_FILE_NAME = "output.json";

    static {
        when(DB_EXECUTOR.executeSelect(
                anyString(),
                anyList(),
                any(ResultSetHandler.class)))
                .thenReturn(Optional.empty());

        given(READER.read(INPUT_ERR_FILE_NAME)).willThrow(IllegalArgumentException.class);
        given(READER.read(INPUT_FILE_NAME)).willReturn(List.of(CRITERIA));
    }

    @Test
    public void getErrorResponseTest() {

        App app = new App(TYPE_REQUEST, INPUT_ERR_FILE_NAME, OUTPUT_FILE_NAME, DB_EXECUTOR);
        Response response = app.getResponse(PROCESSOR);
        assertThat(response).isExactlyInstanceOf(ErrorResponse.class);

        ErrorResponse errorResponse = (ErrorResponse) response;
        assertThat(errorResponse.getType()).isEqualTo(TypeResponse.ERROR.getLowercaseName());
    }

    @Test
    public void getCorrectResponseTest() {
        App app = new App(TYPE_REQUEST, INPUT_FILE_NAME, OUTPUT_FILE_NAME, DB_EXECUTOR);
        Response response = app.getResponse(PROCESSOR);
        assertThat(response).isExactlyInstanceOf(CriteriaResponse.class);

        CriteriaResponse criteriaResponse = (CriteriaResponse) response;
        assertThat(criteriaResponse.getType()).isEqualTo(TypeResponse.SEARCH.getLowercaseName());
        List<CriteriaResponseRowResult> rowResults = criteriaResponse.getResults();
        assertThat(rowResults.size()).isEqualTo(1);

        CriteriaResponseRowResult row = rowResults.get(0);
        assertThat(row.getCriteria()).isEqualTo(CRITERIA);
        assertThat(row.getResults()).isEmpty();
    }

    @Test
    public void setNullTypeErrorTest() {
        assertThatThrownBy(() -> new App(null, INPUT_FILE_NAME, OUTPUT_FILE_NAME, DB_EXECUTOR)
                .getProcessor()).isExactlyInstanceOf(IllegalSearchCriteriaException.class);
    }
}
