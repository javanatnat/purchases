package ru.javanatnat.purchases.criterias;

import java.util.List;

public class MinMaxExpensesCriteriaImpl
        implements Criteria {
    private final int minExpenses;
    private final int maxExpenses;

    // поиск покупателей, у которых общая стоимость всех покупок за всё время попадает в интервал
    private static final String PREP_SQL =
            "select buyers.id as id, " +
                    "buyers.firstname as firstname, " +
                    "buyers.lastname as lastname " +
            "from ( select t_purchases.buyer_id as buyer_id " +
                    "from ( select buyer_id, " +
                                "product_id, " +
                                "count(product_id) as count_product " +
                            "from purchases " +
                            "group by buyer_id, product_id ) as t_purchases " +
                        "join products " +
                    "on t_purchases.product_id = products.id " +
                    "group by t_purchases.buyer_id " +
                    "having sum(t_purchases.count_product * products.price) between ? and ?) as t_buyers " +
            "join buyers on t_buyers.buyer_id = buyers.id";

    public MinMaxExpensesCriteriaImpl(int minExpenses, int maxExpenses) {
        checkParams(minExpenses, maxExpenses);
        this.minExpenses = minExpenses;
        this.maxExpenses = maxExpenses;
    }

    private static void checkParams(int minExpenses, int maxExpenses) {
        if (minExpenses < 0) {
            throw new IllegalArgumentException("Значение минимальной стоимости покупки меньше нуля");
        }
        if (maxExpenses < 0) {
            throw new IllegalArgumentException("Значение максимальной стоимости покупки меньше нуля");
        }
        if (minExpenses > maxExpenses) {
            throw new IllegalArgumentException("Значение минимальной стоимости покупки превышает " +
                    "значение максимальной стоимости покупки");
        }
    }

    @Override
    public String getPreparedSql() {
        return PREP_SQL;
    }

    @Override
    public List<Object> getFactParams() {
        return List.of(minExpenses, maxExpenses);
    }

    public int getMinExpenses() {
        return minExpenses;
    }

    public int getMaxExpenses() {
        return maxExpenses;
    }

    @Override
    public String toString() {
        return "MinMaxExpensesCriteriaImpl{" +
                "minExpenses=" + minExpenses +
                ", maxExpenses=" + maxExpenses +
                '}';
    }
}
