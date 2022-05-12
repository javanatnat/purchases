package ru.javanatnat.purchases.criterias;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BadCustomersCriteriaImpl
        implements Criteria {
    @JsonProperty("badCustomers")
    private final int countCustomers;

    //  поиск покупателей, купивших меньше всего товаров.
    //  Возвращается не более, чем указанное число покупателей.
    private static final String PREP_SQL =
            "select buyers.id as id, " +
                    "buyers.firstname as firstname, " +
                    "buyers.lastname as lastname " +
            "from buyers " +
                "join ( select buyer_id, " +
                                "count(id) as count_p " +
                        "from purchases " +
                        "group by (buyer_id) " +
                        "order by count(id) limit ? ) as s_buyers " +
            "on buyers.id = s_buyers.buyer_id";

    public BadCustomersCriteriaImpl(int countCustomers) {
        checkParams(countCustomers);
        this.countCustomers = countCustomers;
    }

    private static void checkParams(int countCustomers) {
        if (countCustomers < 1) {
            throw new IllegalArgumentException("Значение числа пассивных покупателей меньше единицы");
        }
    }
    @Override
    public String getPreparedSql() {
        return PREP_SQL;
    }

    @Override
    public List<Object> getFactParams() {
        return List.of(countCustomers);
    }

    public int getCountCustomers() {
        return countCustomers;
    }

    @Override
    public String toString() {
        return "BadCustomersCriteriaImpl{" +
                "countCustomers=" + countCustomers +
                '}';
    }
}
