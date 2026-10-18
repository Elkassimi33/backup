package com.youssef.http.http;

public class HttpParsingException extends Exception {

    private final HttpStatusCode errorCode;
    public HttpParsingException( HttpStatusCode errorCode) {
        super(errorCode.MESSAGE);
        this.errorCode = errorCode;
    }

    public HttpStatusCode getErrorCode() {
        System.out.println(errorCode);
        return errorCode;
    }

}
