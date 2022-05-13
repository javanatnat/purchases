package ru.javanatnat.purchases.statistics;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.javanatnat.purchases.request.CustomSqlDateDeserializer;

import java.sql.Date;
import java.util.List;

public class StatisticIntervalCriteriaImpl {
    @JsonDeserialize(using = CustomSqlDateDeserializer.class)
    private Date startDate;
    @JsonDeserialize(using = CustomSqlDateDeserializer.class)
    private Date endDate;

    private static final long COUNT_HOURS = 24;
    private static final long COUNT_MINUTES = 60;
    private static final long COUNT_SECONDS = 60;
    private static final long COUNT_MILLISECONDS = 1000;

    private static final String PREP_SQL =
            "select buyers.id as buyer_id, " +
                    "buyers.firstname as firstname, " +
                    "buyers.lastname as lastname, " +
                    "products.id as product_id, " +
                    "products.name as product_name, " +
                    "(t_purchases.count_buy * products.price) as product_expenses, " +
                    "sum (t_purchases.count_buy * products.price) over (partition by buyers.id) as product_total_expenses " +
            "from ( select buyer_id, " +
                    "product_id, " +
                    "count(product_id) as count_buy " +
                    "from purchases " +
                    "where buy_date between ? and ? " +
                    "group by buyer_id, " +
                        "product_id) as t_purchases " +
                "join buyers " +
            "on t_purchases.buyer_id = buyers.id " +
                "join products " +
            "on t_purchases.product_id = products.id " +
            "order by product_total_expenses desc, " +
                    "product_expenses desc";

    public StatisticIntervalCriteriaImpl() {}

    public StatisticIntervalCriteriaImpl(Date startDate, Date endDate) {
        checkParams(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private static void checkParams(Date startDate, Date endDate) {
        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("Значение начальной даты больше конечной даты");
        }
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
        if (!(this.endDate == null)) {
            checkParams(this.startDate, this.endDate);
        }
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
        if (!(this.startDate == null)) {
            checkParams(this.startDate, this.endDate);
        }
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getPreparedSql() {
        return PREP_SQL;
    }

    public List<Object> getFactParams() {
        return List.of(startDate, endDate);
    }

    public int getCountIntervalDays() {
        long diff = getEndDate().getTime() - getStartDate().getTime();
        long countDays = diff / (COUNT_HOURS * COUNT_MINUTES * COUNT_SECONDS * COUNT_MILLISECONDS);
        int days = (int) countDays;
        return (days + 1);
    }

    @Override
    public String toString() {
        return "StatisticIntervalCriteriaImpl{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (! (o instanceof StatisticIntervalCriteriaImpl criteria)) {
            return false;
        }

        if (!startDate.equals(criteria.startDate)) {
            return false;
        }

        return endDate.equals(criteria.endDate);
    }

    @Override
    public int hashCode() {
        int result = startDate.hashCode();
        result = 31 * result + endDate.hashCode();
        return result;
    }
}
