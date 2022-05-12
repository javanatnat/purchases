package ru.javanatnat.purchases.criterias;

import ru.javanatnat.purchases.search.ProcessDataException;
import ru.javanatnat.purchases.search.Buyer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CriteriaSqlResultSetProcessor {

    private static final String ID = "id";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";

    static List<Buyer> getResult(ResultSet rs) {
        var buyers = new ArrayList<Buyer>();
        try {
            while (rs.next()) {
                buyers.add(new Buyer(
                        rs.getLong(ID),
                        rs.getString(FIRSTNAME),
                        rs.getString(LASTNAME)));
            }
        } catch (SQLException e) {
            throw new ProcessDataException(e);
        }
        return buyers;
    }
}
