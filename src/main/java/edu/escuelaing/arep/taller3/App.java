package edu.escuelaing.arep.taller3;

import static edu.escuelaing.arep.taller3.HttpServer.staticfiles;
import static edu.escuelaing.arep.taller3.controller.NoteControllerImpl.get;
import static edu.escuelaing.arep.taller3.controller.NoteControllerImpl.post;

import edu.escuelaing.arep.taller3.services.NoteServicesImpl;

public class App {

    public static final NoteServicesImpl noteServices = new NoteServicesImpl();

    public static void main(String[] args) {
        staticfiles("target/classes/webroot");

        get("/note", (req, res) -> {
            return noteServices.getNotesAsJSON();
        });

        get("/pi", (req, resp) -> {
            return String.valueOf(Math.PI);
        });

        post("/note", (req, res) -> {
            String title = req.getQueryParams().get("title");
            String group = req.getQueryParams().get("group");
            String content = req.getQueryParams().get("content");
            try {
                noteServices.addNote(title, group, content);
                return "{ \"title\": " + "\"" + title + "\", " + "\"group\": " + "\"" + group + "\", "
                        + "\"content\": " + "\"" + content + "\" " + "}";
            } catch (Exception e) {
                return "{ \"error\": " + "\"" + e.getMessage() + "\"}";
            }
        });

        HttpServer.runServer();

    }
}
