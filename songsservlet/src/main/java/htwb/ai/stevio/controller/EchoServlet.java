package htwb.ai.stevio.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EchoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private String uriToDB = null;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

        this.uriToDB = servletConfig.getInitParameter("uriToDB");
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {


        ObjectMapper mapper = new ObjectMapper();
        Enumeration<String> paramNames = request.getParameterNames();

        String responseStr = "";
        String param = "";
        while (paramNames.hasMoreElements()) {
            param = paramNames.nextElement();
            responseStr = responseStr + param + "="
                    + request.getParameter(param) + "\n";
        }
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");
            if (param.equals("songId")) {
                if (request.getParameter(param).matches("-?\\d+")) {
                    if (InsertSystem.getSong(Integer.parseInt(request.getParameter(param))) != null) {
                        out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(InsertSystem.getSong(Integer.parseInt(request.getParameter(param)))));
                        response.setStatus(200);
                    } else {
                        out.println("Can't find entry");
                        response.setStatus(404);
                    }
                } else {
                    out.println("Wrong body");
                    response.setStatus(400);
                }
            } else if (param.equals("all")) {
                List<Song> songList = InsertSystem.getSongs();
                out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(songList));
                response.setStatus(200);
            } else
                response.setStatus(400);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String body = request.getReader().lines()
                .reduce("", (accumulator, actual) -> accumulator + actual);
        PrintWriter out = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = body;
        try {
            if (body != null) {
                if (body.contains("title")) {
                    Song song = mapper.readValue(jsonInString, Song.class);
                    if (song.getTitle().isEmpty()) {
                        out.println("Wrong body");
                        response.setStatus(400);
                    } else {
                        InsertSystem.addSong(song);
                        response.setStatus(201);
                        response.setHeader("Location", "/songsservlet-stevio/songs?songId=" + song.getId());
                    }
                } else {
                    out.println("Body does not contain necessary information");
                    response.setStatus(400);
                }
            } else {
                out.println("Body does not contain necessary information");
                response.setStatus(400);
            }
        } catch (Exception e) {
            out.println("Wrong Body");
            response.setStatus(400);
        }
    }

    protected String getUriToDB() {
        return this.uriToDB;
    }


    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) {

        response.setStatus(405);
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) {

        response.setStatus(405);
    }

}
