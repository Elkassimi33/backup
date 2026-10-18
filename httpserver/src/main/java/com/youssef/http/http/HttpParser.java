package com.youssef.http.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpParser {
    private final Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    private static  final int SP=0x20;
    private static  final int CR=0x0D;
    private static  final int LF=0x0A;

    public HttpRequest parseHttpRequest(InputStream inputStream) throws  HttpParsingException {

        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);

        HttpRequest request = new HttpRequest();

        try {
            paseRequestLine(reader,request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        parseHeader(reader,request);
        parseBody(reader,request);

        return request;
    }

    private void paseRequestLine(InputStreamReader reader, HttpRequest request) throws IOException, HttpParsingException {

        StringBuilder processingDataBuffer = new StringBuilder();

        boolean methodParsed = false;
        boolean requestTargetParsed = false;
        int _byte;
        while((_byte=reader.read())>=0){
            if(_byte==CR){
                _byte=reader.read();
                if(_byte==LF){
                    LOGGER.debug("Request Version to process : {}", processingDataBuffer.toString());
                    if(!methodParsed || !requestTargetParsed){
                        throw  new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }
                    return;
                }
            }
            if(_byte==SP){

                if(!methodParsed){
                    LOGGER.debug("Processing request method to process : {}", processingDataBuffer.toString());
                    methodParsed = true;

                    request.setMethod(processingDataBuffer.toString());
                }
                else if(!requestTargetParsed){
                    LOGGER.debug("Processing request target to process : {}", processingDataBuffer.toString());
                    requestTargetParsed = true;
                }
                else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
                processingDataBuffer.delete(0,processingDataBuffer.length());
            }
            else{
                processingDataBuffer.append((char)_byte);
                if (!methodParsed) {
                    int test=HttpStatusCode.MAX_LENGTH;
                    if(processingDataBuffer.length()>HttpStatusCode.MAX_LENGTH)
                        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
                }
            }

        }
    }

    private void parseHeader(InputStreamReader reader, HttpRequest request) {
    }

    private void parseBody(InputStreamReader reader, HttpRequest request) {
    }


}
