package com.stanislav.todoappjava.controllers;

import com.stanislav.todoappjava.interfaces.INotesService;
import com.stanislav.todoappjava.models.NoteModel;
import com.stanislav.todoappjava.services.NotesService;
import com.stanislav.todoappjava.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class NoteController {

    @FXML
    public Text title;

    @FXML
    public Text content;

    @FXML
    public ScrollPane scrollPane;

    @FXML
    public Button backButton;

    @FXML
    public TextField titleField;

    @FXML
    public TextArea contentField;

    @FXML
    public VBox contentEditContainer;

    private final INotesService notesService;

    private boolean isEditing = false;

    private NoteModel note;

    @FXML
    private void initialize() {
        content.wrappingWidthProperty().bind(scrollPane.widthProperty().subtract(20));
    }

    public NoteController() {
        this.notesService = new NotesService();
    }

    public void loadNoteById(int noteId) {
        var note = this.notesService.getNoteByID(noteId);
        this.title.setText(note.getTitle());
        this.content.setText(note.getContent());
        this.note = note;
    }

    public void onBackBtnClick(MouseEvent mouseEvent) {
        SceneManager.getMainController().showNotes();
    }

    public void onEditBtnClick(MouseEvent mouseEvent) {
        this.isEditing = !this.isEditing;

        if (isEditing) {
            this.title.setVisible(false);
            this.content.setVisible(false);
            this.titleField.setVisible(true);
            this.contentEditContainer.setVisible(true);
            this.titleField.setText(this.note.getTitle());
            this.contentField.setText(this.note.getContent());
        } else {
            this.title.setVisible(true);
            this.content.setVisible(true);
            this.titleField.setVisible(false);
            this.contentEditContainer.setVisible(false);
        }
    }


    public void onCancelClick() {
        this.isEditing = false;
        this.title.setVisible(true);
        this.content.setVisible(true);
        this.titleField.setVisible(false);
        this.contentEditContainer.setVisible(false);
    }

    public void onSaveClick(MouseEvent mouseEvent) {
        var content = this.contentField.getText();
        var title = this.titleField.getText();

        this.notesService.updateNoteContentById(this.note.id, title, content);
        this.note.setContent(content);
        this.note.setTitle(title);
        this.title.setText(title);
        this.content.setText(content);
        this.onCancelClick();
    }
}
