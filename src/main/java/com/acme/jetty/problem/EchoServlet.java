package main.java.com.acme.jetty.problem;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
class EchoServlet extends HttpServlet {

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getMethod().equals("POST")) {
            response.setContentType(request.getContentType());
            Reader reader = request.getReader();
            Writer writer = response.getWriter();
            char[] buffer = new char[2048];
            int nread;
            while ((nread = reader.read(buffer)) > 0) {
                writer.write(buffer, 0, nread);
            }
        }
    }
}
