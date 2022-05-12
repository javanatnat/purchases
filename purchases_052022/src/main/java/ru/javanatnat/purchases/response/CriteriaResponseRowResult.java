package ru.javanatnat.purchases.response;

import ru.javanatnat.purchases.criterias.Criteria;
import ru.javanatnat.purchases.search.Buyer;

import java.util.ArrayList;
import java.util.List;

public class CriteriaResponseRowResult {
    private final Criteria criteria;
    private final List<Buyer> results;

    public CriteriaResponseRowResult(Criteria criteria) {
        this.criteria = criteria;
        this.results = new ArrayList<>();
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public List<Buyer> getResults() {
        return results;
    }

    public void addBuyer(Buyer buyer) {
        results.add(buyer);
    }

    public void addBuyers(List<Buyer> buyers) {
        results.addAll(buyers);
    }
}
