package ru.javanatnat.purchases.statistics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javanatnat.purchases.search.ProcessDataException;
import ru.javanatnat.purchases.search.Buyer;
import ru.javanatnat.purchases.search.BuyerStatResult;
import ru.javanatnat.purchases.search.ProductPurchases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StatSqlResultSetProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(StatSqlResultSetProcessor.class);

    private static final String BUYER_ID = "buyer_id";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String TOTAL_EXPENSES = "product_total_expenses";
    private static final String PRODUCT_ID = "product_id";
    private static final String PRODUCT_NAME = "product_name";
    private static final String PRODUCT_EXPENSES = "product_expenses";

    static List<BuyerStatResult> getResult(ResultSet rs) {
        LOG.debug("start parse stat result...");
        var result = new ArrayList<BuyerStatResult>();
        Buyer buyer = null;
        BuyerStatResult rowResult = null;

        try {
            while (rs.next()) {
                Long buyerId = rs.getLong(BUYER_ID);
                if (buyer == null || !Objects.equals(buyer.getId(), buyerId)) {
                    buyer = new Buyer(
                            buyerId,
                            rs.getString(FIRSTNAME),
                            rs.getString(LASTNAME));
                    rowResult = new BuyerStatResult(
                            buyer,
                            rs.getInt(TOTAL_EXPENSES));
                    result.add(rowResult);

                    LOG.debug("buyer: {}", rowResult.getBuyer());
                    LOG.debug("total expenses: {}", rowResult.getTotalExpenses());
                }
                var product = new ProductPurchases(
                        rs.getLong(PRODUCT_ID),
                        rs.getString(PRODUCT_NAME),
                        rs.getInt(PRODUCT_EXPENSES));
                rowResult.addProductPurchases(product);
                LOG.debug("product: {}", product);
            }
        } catch (SQLException e) {
            throw new ProcessDataException(e);
        }
        LOG.debug("parsing stat result ends successfully");
        return result;
    }
}
