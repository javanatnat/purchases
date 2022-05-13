package ru.javanatnat.purchases;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class AppYml {
    private static final String FILEPATH = "src/main/resources/application.yml";
    private DatasourceYml datasource;

    public AppYml() {}

    public void setDatasource(DatasourceYml datasource) {
        this.datasource = datasource;
    }

    public String getUrl() {
        return datasource.getUrl();
    }

    public String getUsername() {
        return datasource.getUsername();
    }

    public String getPassword() {
        return datasource.getPassword();
    }

    public static AppYml getInstance() {
        return AppYmlHolder.APP_YML;
    }

    private static class AppYmlHolder {
        static final AppYml APP_YML;

        static {
            var mapper = new ObjectMapper(new YAMLFactory());
            mapper.findAndRegisterModules();
            try {
                APP_YML = mapper.readValue(new File(FILEPATH), AppYml.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
