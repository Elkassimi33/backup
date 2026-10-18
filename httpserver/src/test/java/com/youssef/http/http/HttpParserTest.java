package com.youssef.http.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {

    private HttpParser httpParser ;

    @BeforeAll
    public  void beforeClass(){
        httpParser = new HttpParser();
    }
    @Test
    void parseHttpRequest()  {

        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateValideGETTestCase());
        } catch (HttpParsingException e) {
            fail(e);
        }

        assert(request.getMethod()== HttpMethod.GET);
    }

    private InputStream generateValideGETTestCase(){

        String rawData="GET / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "sec-ch-ua: \"Brave\";v=\"141\", \"Not?A_Brand\";v=\"8\", \"Chromium\";v=\"141\"\r\n" +
                "sec-ch-ua-mobile: ?0\r\n" +
                "sec-ch-ua-platform: \"Windows\"\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/141.0.0.0 Safari/537.36\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8\r\n" +
                "Sec-GPC: 1\r\n" +
                "Accept-Language: fr-FR,fr;q=0.6\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n"+
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));

        return inputStream;
    }
    @Test
    void parseHttpBadRequest()  {

        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateValideBadTestCase());
            fail();
        } catch (HttpParsingException e) {
            assertEquals( e.getErrorCode(),HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);

        }


    }

    private InputStream generateValideBadTestCase() {
        String rawData="tes / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "\r\n";

        return new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
    }


}
