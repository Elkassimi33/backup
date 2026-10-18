package com.youssef.http.http;

public enum HttpStatusCode {
    /*  -----CLIENT ERROR----- */
    CLIENT_ERROR_400_BAD_REQUEST(400,"BadRequest"),
    CLIENT_ERROR_401_METHOD_NOT_ALLOWED(401,"Method Not Allowed"),
    CLIENT_ERROR_414_BAD_REQUEST(400,"URI too long"),
    /*  -----SERVER ERRORS----- */
    SERVER_ERROR_500_INTERNAL_SERVER_ERROR(500,"Internal Server Error"),
    SERVER_ERROR_501_NOT_IMPLEMENTED(501,"Not Implemented");

    public static final int MAX_LENGTH ;
    public final int STATUS_CODE;
    public final String MESSAGE;

    HttpStatusCode( int STATUS_CODE, String MESSAGE) {
        this.STATUS_CODE = STATUS_CODE;
        this.MESSAGE = MESSAGE;
    }
    static{
        int tempMaxLength=-1;
        for(HttpStatusCode statusCode : HttpStatusCode.values()){
            if(tempMaxLength<statusCode.name().length()){
                tempMaxLength=statusCode.name().length();
            }
        }
        MAX_LENGTH=tempMaxLength;
    }
}
