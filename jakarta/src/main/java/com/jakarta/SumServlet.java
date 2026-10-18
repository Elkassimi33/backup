package com.jakarta;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/sum")
public class SumServlet extends HttpServlet {

    public void service(HttpServletRequest req, HttpServletResponse res) throws  IOException {

        System.out.println("dfcgvhjkasvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvsvasdgvg "+req.getParameter("num1"));

        int num1 = Integer.parseInt(req.getParameter("num1"));
        int num2 = Integer.parseInt(req.getParameter("num2"));
        int result = num1 + num2;

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        System.out.println(result);
        String page = """

                <!DOCTYPE html>
                <html>
                <body>
                <h1>calculator Page</h1>
                <form action="sum">
                Enter 1 number: <input type="number" name="num1" value="%d"/>
                    <br>
                Enter 2 number: <input type="number" name="num2" value="%d">
                    <br>
                     result :%d
                    <button type="submit">submit</button>
                </form>
                </body>
                </html>""".formatted(num1,num2,result);
        out.println(page);
    }


}
