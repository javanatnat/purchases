package ru.javanatnat.purchases.criterias;

import java.util.List;
import java.util.Objects;

public class ProductMinTimesCriteriaImpl
        implements Criteria {

    private final String productName;
    private final int minTimes;

    // поиск покупателей, купивших этот товар не менее, чем указанное число раз
    private static final String PREP_SQL =
            "select buyers.id as id, " +
                    "buyers.firstname as firstname, " +
                    "buyers.lastname as lastname " +
            "from buyers " +
                "join ( select buyer_id " +
                        "from purchases " +
                        "where product_id = (select id from products where name = ?) " +
                        "group by (buyer_id) " +
                        "having count(id) > ? ) as p_buyers " +
            "on buyers.id = p_buyers.buyer_id";

    public ProductMinTimesCriteriaImpl(String productName, int minTimes) {
        checkParams(productName, minTimes);
        this.productName = productName;
        this.minTimes = minTimes;
    }

    private static void checkParams(String productName, int minTimes) {
        Objects.requireNonNull(productName);
        if (minTimes < 1) {
            throw new IllegalArgumentException("Значение минимального количества раз для покупки меньше единицы");
        }
    }

    @Override
    public String getPreparedSql() {
        return PREP_SQL;
    }

    @Override
    public List<Object> getFactParams() {
        return List.of(productName, minTimes);
    }

    public String getProductName() {
        return productName;
    }

    public int getMinTimes() {
        return minTimes;
    }

    @Override
    public String toString() {
        return "ProductMinTimesCriteriaImpl{" +
                "productName='" + productName + '\'' +
                ", minTimes=" + minTimes +
                '}';
    }
}
