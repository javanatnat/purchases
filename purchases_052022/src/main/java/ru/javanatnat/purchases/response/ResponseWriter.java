package ru.javanatnat.purchases.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javanatnat.purchases.Response;

import java.io.File;
import java.io.IOException;

public class ResponseWriter {
    private static final Logger LOG = LoggerFactory.getLogger(ResponseWriter.class);
    private final ObjectMapper OBJECT_MAPPER;
    private final Response response;

    public ResponseWriter(Response response) {
        OBJECT_MAPPER = ObjectMapperHolder.OBJECT_MAPPER;
        this.response = response;
    }

    public void write(String fileName) {
        LOG.info("write response to file: {}", fileName);
        File outputFile = new File(fileName);
        try {
            OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(outputFile, response);
            LOG.info("success write response");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class ObjectMapperHolder {
        static final ObjectMapper OBJECT_MAPPER;

        static {
            OBJECT_MAPPER = new ObjectMapper();
            OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        }
    }
}
