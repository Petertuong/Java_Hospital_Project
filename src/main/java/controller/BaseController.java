package controller;

import com.google.gson.Gson;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import javax.servlet.http.HttpServlet;

public abstract class BaseController extends HttpServlet {

    protected Gson gson = new Gson();

    protected void sendJson(HttpServletResponse resp, Object data) throws IOException{
        
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        out.print(gson.toJson(data));
        out.flush();

    }

    protected void sendError(HttpServletResponse resp, int statusCode, String msg) throws IOException{
        resp.setStatus(statusCode);
        resp.setContentType("application/json");
        resp.getWriter().write("{\"error\": \"" +msg + "\"}");
    }

    // Helper 3: Read JSON from Request and convert to Java Object
    protected <T> T readJson(HttpServletRequest req, Class<T> clazz) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return gson.fromJson(sb.toString(), clazz);
    }
}
