package ru.javanatnat.purchases.criterias;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MinMaxExpensesCriteriaTest {

    @Test
    public void correctCreateTest() {
        int minExpenses = 100;
        int maxExpenses = 200;

        MinMaxExpensesCriteriaImpl criteria = new MinMaxExpensesCriteriaImpl(minExpenses, maxExpenses);
        assertThat(criteria.getMinExpenses()).isEqualTo(minExpenses);
        assertThat(criteria.getMaxExpenses()).isEqualTo(maxExpenses);
        assertThat(criteria.getFactParams()).containsExactly(minExpenses, maxExpenses);
        assertThat(criteria.getPreparedSql()).isNotEmpty();
    }

    @Test
    public void incorrectCreateTest() {
        int minExpenses = 1000;
        int maxExpenses = 200;

        assertThatThrownBy(() -> new MinMaxExpensesCriteriaImpl(minExpenses, maxExpenses))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
