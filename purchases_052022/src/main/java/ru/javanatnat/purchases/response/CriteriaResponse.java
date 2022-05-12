package ru.javanatnat.purchases.response;

import ru.javanatnat.purchases.Response;

import java.util.ArrayList;
import java.util.List;

public class CriteriaResponse implements Response {
    private final String type;
    private final List<CriteriaResponseRowResult> results;

    public CriteriaResponse(String type) {
        this.type = type;
        this.results = new ArrayList<>();
    }

    public String getType() {
        return type;
    }

    public List<CriteriaResponseRowResult> getResults() {
        return results;
    }

    public void addRowResult(CriteriaResponseRowResult rowResult) {
        results.add(rowResult);
    }

    public void addRowResults(List<CriteriaResponseRowResult> rows) {
        results.addAll(rows);
    }
}
