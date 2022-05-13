package ru.javanatnat.purchases.search;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class BuyerStatResult {
    @JsonIgnore
    private final Buyer buyer;
    private final String name;
    private final List<ProductPurchases> purchases;
    private final int totalExpenses;

    public BuyerStatResult(
            Buyer buyer,
            List<ProductPurchases> purchases,
            int totalExpenses
    ) {
        this.buyer = buyer;
        this.name = this.buyer.getUnionName();
        this.purchases = purchases;
        this.totalExpenses = totalExpenses;
    }

    public BuyerStatResult(
            Buyer buyer,
            int totalExpenses
    ) {
        this(buyer, new ArrayList<>(), totalExpenses);
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public String getName() {
        return name;
    }

    public List<ProductPurchases> getPurchases() {
        return purchases;
    }

    public int getTotalExpenses() {
        return totalExpenses;
    }

    public void addProductPurchases(ProductPurchases purchases) {
        this.purchases.add(purchases);
    }

    @Override
    public String toString() {
        return "BuyerStatResult{" +
                "buyer=" + buyer +
                ", name=" + name +
                ", purchases=" + purchases +
                ", totalExpenses=" + totalExpenses +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (! (o instanceof BuyerStatResult buyerStatResult)) {
            return false;
        }

        if (totalExpenses != buyerStatResult.totalExpenses) {
            return false;
        }

        if (!buyer.equals(buyerStatResult.buyer)) {
            return false;
        }

        if (!name.equals(buyerStatResult.name)) {
            return false;
        }

        return purchases.equals(buyerStatResult.purchases);
    }

    @Override
    public int hashCode() {
        int result = buyer.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + purchases.hashCode();
        result = 31 * result + totalExpenses;
        return result;
    }
}
