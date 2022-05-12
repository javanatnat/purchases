package ru.javanatnat.purchases.response;

import ru.javanatnat.purchases.Response;

public class ErrorResponse implements Response {
    private final String type;
    private final String message;

    private ErrorResponse(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public static ErrorResponse of(String message) {
        return new ErrorResponse(TypeResponse.ERROR.getLowercaseName(), message);
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "type='" + type + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
