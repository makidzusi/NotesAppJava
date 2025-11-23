package com.stanislav.todoappjava.controllers;

import com.stanislav.todoappjava.interfaces.INotesService;
import com.stanislav.todoappjava.services.NotesService;
import com.stanislav.todoappjava.utils.CustomEvent;
import com.stanislav.todoappjava.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AddNoteController {

    @FXML
    private TextField titleField;

    @FXML
    private TextArea contentField;

    @FXML
    private void initialize() {}

    @FXML
    private void onCancelClick() {
       this.titleField.getScene().getWindow().fireEvent(new CustomEvent(CustomEvent.OPEN_NOTES_VIEW));
    }

    private final INotesService notesService;

    public AddNoteController() {
        this.notesService = new NotesService();
    }

    @FXML
    private void onSaveClick() {
        String title = titleField.getText().trim();
        String content = contentField.getText().trim();

        if (title.isEmpty() || content.isEmpty()) return;

        this.notesService.addNote(content,title, SessionManager.getCurrentUser().getId());

        this.titleField.getScene().getWindow().fireEvent(new CustomEvent(CustomEvent.OPEN_NOTES_VIEW));

    }
}
