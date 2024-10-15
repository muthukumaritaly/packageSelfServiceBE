package com.example.config;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        String body = "Unknown error";

        try {
            if (response.body() != null) {
                body = Util.toString(response.body().asReader(Util.UTF_8));
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        HttpStatus status = HttpStatus.resolve(response.status());
        if (status != null) {
            return new ResponseStatusException(status, body);
        }

        return new Exception("General Error: " + body);
    }
}
