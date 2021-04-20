package de.jdungeon.backend.highscore;

import com.badlogic.gdx.utils.Json;
import de.jdungeon.score.SessionScore;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

//@WebServlet(name = "Highscore", value = "/get")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Here is your Highscore!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        List<String> strings = IOUtils.readLines(reader);
        String content = String.join("", strings);
        //Person person2 = json.fromJson(Person.class, text);
        Json json = new Json();
        SessionScore score = json.fromJson(SessionScore.class, content);

        String currentExecutablePath = System.getProperty("user.dir");
        File file = new File("/home/tomcat/scoredata", createFilename(score));
        FileUtils.write(file, content, "UTF-8");

        resp.setStatus(200);
    }

    static String createFilename(SessionScore score) {
        return score.getStartTime() + "_"+score.getPlayerName()+"_"+score.getSessionID()+".json";
    }

    public void destroy() {
    }
}