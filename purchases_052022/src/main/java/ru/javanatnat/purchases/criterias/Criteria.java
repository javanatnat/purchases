package ru.javanatnat.purchases.criterias;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public interface Criteria {
    @JsonIgnore
    String getPreparedSql();
    @JsonIgnore
    List<Object> getFactParams();
}
