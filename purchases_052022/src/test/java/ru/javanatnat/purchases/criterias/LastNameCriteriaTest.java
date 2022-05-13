package ru.javanatnat.purchases.criterias;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class LastNameCriteriaTest {
    @Test
    public void correctCreateTest() {
        String lastName = "Ivanov";
        LastNameCriteriaImpl criteria = new LastNameCriteriaImpl(lastName);

        assertThat(criteria.getLastName())
                .isEqualTo(lastName);

        assertThat(criteria.getFactParams())
                .isEqualTo(List.of(lastName));

        assertThat(criteria.getPreparedSql()).isNotEmpty();
    }

    @Test
    public void incorrectCreateTest() {
        assertThatThrownBy(() -> new LastNameCriteriaImpl(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }
}
