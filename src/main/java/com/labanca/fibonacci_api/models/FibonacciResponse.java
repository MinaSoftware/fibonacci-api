package com.labanca.fibonacci_api.models;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class FibonacciResponse {
    private Long position;
    private Long result;
    private String error;
    private HttpStatusCode httpStatusCode;

    public FibonacciResponse() {}

    public FibonacciResponse(Long position, Long result) {
        this.position = position;
        this.result = result;
        this.httpStatusCode = HttpStatus.OK;
    }

    public FibonacciResponse(String error, HttpStatus httpStatusCode) {
        this.error = error;
        this.httpStatusCode = httpStatusCode;
    }

    public Long getPosition() {
        return position;
    }


    public Long getResult() {
        return result;
    }


    public String getError() {
        return error;
    }


    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }

}


