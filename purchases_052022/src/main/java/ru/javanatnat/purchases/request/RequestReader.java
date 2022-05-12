package ru.javanatnat.purchases.request;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class RequestReader<T> {
    protected final ObjectMapper objectMapper;

    public RequestReader() {
        this(RequestReader.ObjectMapperHolder.OBJECT_MAPPER);
    }

    public abstract T read(String fileName);

    private RequestReader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private static class ObjectMapperHolder {
        static final ObjectMapper OBJECT_MAPPER;

        static {
            OBJECT_MAPPER = new ObjectMapper();
            OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
        }
    }
}
