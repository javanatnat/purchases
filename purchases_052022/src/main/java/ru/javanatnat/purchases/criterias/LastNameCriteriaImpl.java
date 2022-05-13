package ru.javanatnat.purchases.criterias;

import java.util.List;
import java.util.Objects;

public class LastNameCriteriaImpl
        implements Criteria {

    private final String lastName;

    private static final String PREP_SQL =
            "select id, firstname, lastname from buyers where lastname = ?";

    public LastNameCriteriaImpl(String lastName) {
        Objects.requireNonNull(lastName);
        this.lastName = lastName;
    }

    @Override
    public String getPreparedSql() {
        // поиск покупателей с этой фамилией
        return PREP_SQL;
    }

    @Override
    public List<Object> getFactParams() {
        return List.of(lastName);
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "LastNameCriteriaImpl{" +
                "lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (! (o instanceof LastNameCriteriaImpl criteria)) {
            return false;
        }

        return lastName.equals(criteria.lastName);
    }

    @Override
    public int hashCode() {
        return lastName.hashCode();
    }
}
