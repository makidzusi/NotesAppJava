package com.stanislav.todoappjava.services;

import com.stanislav.todoappjava.models.NoteModel;
import com.stanislav.todoappjava.repositories.NotesRepository;

import java.util.List;

public class NotesService implements com.stanislav.todoappjava.interfaces.INotesService {

    private final NotesRepository notesRepository;

    public NotesService() {
        this.notesRepository = new NotesRepository();

    }

    @Override
    public void addNote(String content, String title, Integer userId) {
        this.notesRepository.addNote(title, content, userId);
    }

    @Override
    public NoteModel getNoteByID(int id) {
        return  this.notesRepository.getNoteById(id);
    }

    @Override
    public void toggleNoteFavourite(int noteId, boolean state) {
        this.notesRepository.toggleFavourite(noteId, state);
    }

    @Override
    public void deleteNotById(int noteId) {
            this.notesRepository.deleteNote(noteId);
    }

    @Override
    public void updateNoteContentById(int id, String title, String content) {
        this.notesRepository.updateNoteContentById(id, title, content);
    }

    @Override
    public void toggleNoteIsArchived(int noteId, boolean state) {
        this.notesRepository.toggleIsArchived(noteId, state);
    }

    @Override
    public List<NoteModel> getUserNotes(Integer userId) {
        return this.notesRepository.getAllUserNotes(userId);
    }
}
