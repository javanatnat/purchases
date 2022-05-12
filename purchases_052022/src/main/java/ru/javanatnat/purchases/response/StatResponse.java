package ru.javanatnat.purchases.response;

import ru.javanatnat.purchases.Response;
import ru.javanatnat.purchases.search.BuyerStatResult;

import java.util.ArrayList;
import java.util.List;

public class StatResponse implements Response {
    private final String type;
    private final int totalDays;
    private final List<BuyerStatResult> customers;
    private long totalExpenses;
    private double avgExpenses;

    public StatResponse(String type, int totalDays) {
        this.type = type;
        this.totalDays = totalDays;
        this.customers = new ArrayList<>();
    }

    public String getType() {
        return type;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public List<BuyerStatResult> getCustomers() {
        return customers;
    }

    public long getTotalExpenses() {
        return totalExpenses;
    }

    public double getAvgExpenses() {
        return avgExpenses;
    }

    public void setTotalExpenses(long totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public void setAvgExpenses(double avgExpenses) {
        this.avgExpenses = avgExpenses;
    }

    public void addCustomer(BuyerStatResult customer) {
        customers.add(customer);
    }

    public void addCustomers(List<BuyerStatResult> customers) {
        this.customers.addAll(customers);
    }
}
