package ru.javanatnat.purchases.search;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProductPurchases {
    @JsonIgnore
    private final Long id;
    private final String name;
    private final int expenses;

    public ProductPurchases(Long id, String name, int expenses) {
        this.id = id;
        this.name = name;
        this.expenses = expenses;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getExpenses() {
        return expenses;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", expenses=" + expenses +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (! (o instanceof ProductPurchases productPurchases)) {
            return false;
        }

        if (expenses != productPurchases.expenses) {
            return false;
        }

        if (!id.equals(productPurchases.id)) {
            return false;
        }

        return name.equals(productPurchases.name);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + expenses;
        return result;
    }
}
