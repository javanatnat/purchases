package ru.javanatnat.purchases.criterias;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BadCustomersCriteriaTest {

    @Test
    public void correctCreateTest() {
        int countCustomers = 3;
        BadCustomersCriteriaImpl criteria = new BadCustomersCriteriaImpl(countCustomers);

        assertThat(criteria.getCountCustomers())
                .isEqualTo(countCustomers);

        assertThat(criteria.getFactParams())
                .isEqualTo(List.of(countCustomers));

        assertThat(criteria.getPreparedSql()).isNotEmpty();
    }

    @Test
    public void incorrectCreateTest() {
        int countCustomers = -1;
        assertThatThrownBy(() -> new BadCustomersCriteriaImpl(countCustomers))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
