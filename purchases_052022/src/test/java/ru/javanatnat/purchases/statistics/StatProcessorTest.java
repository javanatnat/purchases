package ru.javanatnat.purchases.statistics;

import org.junit.jupiter.api.Test;
import ru.javanatnat.purchases.Response;
import ru.javanatnat.purchases.datasource.DbExecutor;
import ru.javanatnat.purchases.response.StatResponse;
import ru.javanatnat.purchases.response.TypeResponse;
import ru.javanatnat.purchases.search.Buyer;
import ru.javanatnat.purchases.search.BuyerStatResult;
import ru.javanatnat.purchases.search.ProductPurchases;
import ru.javanatnat.purchases.search.ResultSetHandler;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StatProcessorTest {
    private static final StatReaderImpl READER = mock(StatReaderImpl.class);
    private static final DbExecutor DB_EXECUTOR = mock(DbExecutor.class);
    private static final StatProcessorImpl PROCESSOR = new StatProcessorImpl(DB_EXECUTOR, READER);

    private static final String FILEPATH = "test.json";
    private static final StatisticIntervalCriteriaImpl CRITERIA;

    private static final List<BuyerStatResult> EXECUTOR_RESULTS;
    private static final String LAST_NAME = "Ivanov";
    private static final String FIRST_NAME = "Ivan";
    private static final String MILK_NAME = "milk";
    private static final String WATER_NAME = "water";
    private static final Buyer BUYER_1 = new Buyer(1L, LAST_NAME, FIRST_NAME);
    private static final Buyer BUYER_2 = new Buyer(2L, LAST_NAME, FIRST_NAME);
    private static final ProductPurchases PRODUCT_PURCHASES_11 = new ProductPurchases(1L, MILK_NAME, 70);
    private static final ProductPurchases PRODUCT_PURCHASES_12 = new ProductPurchases(2L, WATER_NAME, 30);
    private static final ProductPurchases PRODUCT_PURCHASES_21 = new ProductPurchases(2L, WATER_NAME, 60);


    static {
        Date startDate = Date.valueOf("2020-01-01");
        Date endDate = Date.valueOf("2020-01-02");

        CRITERIA = new StatisticIntervalCriteriaImpl(startDate, endDate);

        EXECUTOR_RESULTS = List.of(
                new BuyerStatResult(
                        BUYER_1,
                        List.of(PRODUCT_PURCHASES_11,
                                PRODUCT_PURCHASES_12),
                        100),
                new BuyerStatResult(
                        BUYER_2,
                        List.of(PRODUCT_PURCHASES_21),
                        60));

        when(DB_EXECUTOR.executeSelect(
                anyString(),
                anyList(),
                any(ResultSetHandler.class)))
                .thenReturn(Optional.of(EXECUTOR_RESULTS));

        given(READER.read(FILEPATH)).willReturn(CRITERIA);
    }

    @Test
    public void getResponseLastNameCriteriaTest() {
        Response response = PROCESSOR.process(FILEPATH);
        assertThat(response).isExactlyInstanceOf(StatResponse.class);

        StatResponse statResponse = (StatResponse) response;

        assertThat(statResponse.getType()).isEqualTo(TypeResponse.STAT.getLowercaseName());
        assertThat(statResponse.getTotalDays()).isEqualTo(2);
        assertThat(statResponse.getCustomers().size()).isEqualTo(2);
        assertThat(statResponse.getTotalExpenses()).isEqualTo(160);
        assertThat(statResponse.getAvgExpenses()).isEqualTo(80);

        List<BuyerStatResult> buyerStatResults = statResponse.getCustomers();

        assertThat(buyerStatResults.get(0).getBuyer()).isEqualTo(BUYER_1);
        assertThat(buyerStatResults.get(0).getName()).isEqualTo(BUYER_1.getUnionName());
        assertThat(buyerStatResults.get(0).getTotalExpenses()).isEqualTo(100);
        assertThat(buyerStatResults.get(0).getPurchases()).containsExactly(PRODUCT_PURCHASES_11, PRODUCT_PURCHASES_12);

        assertThat(buyerStatResults.get(1).getBuyer()).isEqualTo(BUYER_2);
        assertThat(buyerStatResults.get(1).getName()).isEqualTo(BUYER_2.getUnionName());
        assertThat(buyerStatResults.get(1).getTotalExpenses()).isEqualTo(60);
        assertThat(buyerStatResults.get(1).getPurchases()).containsExactly(PRODUCT_PURCHASES_21);
    }
}
