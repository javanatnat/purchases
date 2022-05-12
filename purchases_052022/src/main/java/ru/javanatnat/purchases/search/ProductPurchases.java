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
}
