package ru.javanatnat.purchases.request;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.sql.Date;

public class CustomSqlDateDeserializer extends StdDeserializer<Date> {

    public CustomSqlDateDeserializer() {
        this(null);
    }

    public CustomSqlDateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException
    {
        String date = jsonParser.getText();
        return Date.valueOf(date);
    }
}
