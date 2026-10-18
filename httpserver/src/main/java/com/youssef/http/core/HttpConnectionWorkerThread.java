package com.youssef.http.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpConnectionWorkerThread extends Thread{

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);
    private Socket socket;

    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        InputStream inputStream =null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

//            int _byte;
//            while((_byte = inputStream.read())>0)
//                System.out.print((char)_byte);

            final String CRLF = "\r\n";  //13 , 10


            String html = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                    <title> java http server </title>
                    </head>
                    <body>
                    <h1>this is my heading</h1>
                    </body>
                    </html>
                    """;
            String response =
                    "HTTP/1.1 200 OK" + CRLF +//Status Line : HTTP VERSION RESPONSE_CODE RESPONSE_MESSAGE
                            "content-length: " + html.getBytes().length + CRLF +//HEADER
                            CRLF +
                            html +
                            CRLF + CRLF;

            outputStream.write(response.getBytes());




            try {
                sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            LOGGER.info("Connection processing finished.");
        } catch (IOException e) {
            LOGGER.error("Connection processing failed.", e);
        }finally {
            try {
                if(inputStream!=null)
                    inputStream.close();
                if(outputStream!=null)
                    outputStream.close();
                if(socket!=null)
                    socket.close();
            } catch (IOException ignored) {}
        }
    }
}
