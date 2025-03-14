package com.soundbrew.soundbrew.handler;

import com.google.gson.Gson;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface ExceptionResponseHandler<T> {

    T getError();

    default void sendResponseError(HttpServletResponse response,
                                   Function<T, Integer> statusProvider,
                                   Function<T, String> messageProvider) {
        T error = getError();

        response.setStatus(statusProvider.apply(error));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        String responseStr = gson.toJson(
                Map.of(
                        "msg", messageProvider.apply(error),
                        "time", new Date()
                )
        );

        try {

            response.getWriter().println(responseStr);

        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }
}
