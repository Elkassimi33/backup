package com.youssef.http.http;

public class HttpRequest extends HttpMessage {

    private HttpMethod method;
    private String requestTarget;
    private String httpVersion;

    HttpRequest(){};

    HttpMethod getMethod() {
        return method;
    }

    void setMethod(String method) throws HttpParsingException {

        for(HttpMethod httpMethod : HttpMethod.values()) {

            if(httpMethod.name().equals(method)) {
                this.method = HttpMethod.valueOf(method);
                return;
            }
        }
        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
    }
}
