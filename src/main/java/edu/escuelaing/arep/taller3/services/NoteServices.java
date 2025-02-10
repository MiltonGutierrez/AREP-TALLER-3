package edu.escuelaing.arep.taller3.services;

import java.util.ArrayList;

import edu.escuelaing.arep.taller3.model.Note;
import edu.escuelaing.arep.taller3.services.exception.NoteServicesException;


public interface NoteServices {
    ArrayList<Note> getNotes();
    void addNote(String title, String group, String content) throws NoteServicesException;
    String getNotesAsJSON();
}
