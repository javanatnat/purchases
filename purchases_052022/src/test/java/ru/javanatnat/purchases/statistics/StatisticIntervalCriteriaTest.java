package ru.javanatnat.purchases.statistics;

import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StatisticIntervalCriteriaTest {

    @Test
    public void correctCreateTest() {
        Date startDate = Date.valueOf("2020-02-20");
        Date endDate = Date.valueOf("2020-02-21");

        StatisticIntervalCriteriaImpl stat = new StatisticIntervalCriteriaImpl(startDate, endDate);
        assertThat(stat.getCountIntervalDays()).isEqualTo(2);
        assertThat(stat.getStartDate()).isEqualTo(startDate);
        assertThat(stat.getEndDate()).isEqualTo(endDate);
        assertThat(stat.getFactParams()).containsExactly(startDate, endDate);
        assertThat(stat.getPreparedSql()).isNotEmpty();
    }

    @Test
    public void incorrectCreateTest() {
        Date startDate = Date.valueOf("2020-02-25");
        Date endDate = Date.valueOf("2020-02-21");

        assertThatThrownBy(() -> new StatisticIntervalCriteriaImpl(startDate, endDate))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
