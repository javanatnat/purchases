package ru.javanatnat.purchases.criterias;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductMinTimesCriteriaTest {

    @Test
    public void correctCreateTest() {
        String productName = "milk";
        int minTimes = 2;

        ProductMinTimesCriteriaImpl criteria = new ProductMinTimesCriteriaImpl(productName, minTimes);
        assertThat(criteria.getProductName()).isEqualTo(productName);
        assertThat(criteria.getMinTimes()).isEqualTo(minTimes);
        assertThat(criteria.getFactParams()).containsExactly(productName, minTimes);
        assertThat(criteria.getPreparedSql()).isNotEmpty();
    }

    @Test
    public void incorrectCreateTest() {
        String productName = "milk";
        int minTimes = 0;
        int correctMinTimes = 10;

        assertThatThrownBy(() -> new ProductMinTimesCriteriaImpl(productName, minTimes))
                .isExactlyInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new ProductMinTimesCriteriaImpl(null, correctMinTimes))
                .isExactlyInstanceOf(NullPointerException.class);
    }
}
