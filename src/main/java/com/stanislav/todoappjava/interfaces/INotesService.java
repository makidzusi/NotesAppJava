package com.stanislav.todoappjava.interfaces;

import com.stanislav.todoappjava.models.NoteModel;

import java.util.List;

public interface INotesService {
    void addNote(String content, String title, Integer userId);

    NoteModel getNoteByID(int id);

    void toggleNoteFavourite(int noteId, boolean state);

    void deleteNotById(int noteId);

    void updateNoteContentById(int id, String title, String content);

    void toggleNoteIsArchived(int noteId, boolean state);

    List<NoteModel> getUserNotes(Integer userId);
}
