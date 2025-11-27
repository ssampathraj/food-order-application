package com.MicroService.order.decoder;

import feign.Response;
import feign.codec.ErrorDecoder;
import feign.RetryableException;
import java.util.Date;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {

        int status = response.status();

        switch (status) {

            case 400:
                return new BadRequestException("Bad Request from server");

            case 401:
                return new UnauthorizedException("Unauthorized access");

            case 403:
                return new ForbiddenException("Forbidden request");

            case 404:
                return new NotFoundException("Resource not found");

            case 429:
                // Too many requests → Retry later
                return new RetryableException(
                        status,
                        "Rate limited. Retry later",
                        response.request().httpMethod(),
                        null,
                        new Date(System.currentTimeMillis() + 2000), // retry after 2s
                        response.request());

            case 500:
                return new InternalServerErrorException("Internal Server Error");

            case 503:
                // Service unavailable → Retry
                return new RetryableException(
                        status,
                        "Service unavailable",
                        response.request().httpMethod(),
                        null,
                        new Date(System.currentTimeMillis() + 1000),
                        response.request());

            default:
                return defaultDecoder.decode(methodKey, response);  // fallback
        }
    }
}

