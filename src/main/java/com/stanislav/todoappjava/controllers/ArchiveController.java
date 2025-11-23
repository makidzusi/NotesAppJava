package com.stanislav.todoappjava.controllers;

import com.stanislav.todoappjava.interfaces.INotesService;
import com.stanislav.todoappjava.services.NotesService;
import com.stanislav.todoappjava.utils.CustomEvent;
import com.stanislav.todoappjava.utils.SceneManager;
import com.stanislav.todoappjava.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import com.stanislav.todoappjava.models.NoteModel;
import javafx.scene.text.Text;

import java.util.List;

public class ArchiveController {

    @FXML
    public TextField textField;

    @FXML
    private TilePane notesContainer;

    private List<NoteModel> notes;

    private final INotesService notesService;

    public ArchiveController() {
        this.notesService = new NotesService();
    }

    @FXML
    private void initialize() {
        SceneManager.primaryStage.getScene().getWindow().addEventHandler(CustomEvent.REFRESH_NOTES, e -> refreshNotes());

        this.refreshNotes();

        this.textField.textProperty().addListener((obs, oldText, newText) -> {
            String filter = newText.toLowerCase();
            List<NoteModel> filtered = this.notes.stream()
                    .filter(note -> note.getTitle().toLowerCase().contains(filter))
                    .toList();
            updateNotes(filtered);
        });

    }

    private void refreshNotes() {
        this.notes = this.notesService.getUserNotes(SessionManager.getCurrentUser().getId()).stream().filter(NoteModel::isArchived).toList();
        this.updateNotes(this.notes);
    }

    private void updateNotes(List<NoteModel> notes) {
        notesContainer.getChildren().clear();
        for (NoteModel note : notes) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/stanislav/todoappjava/ui/note-card.fxml"));
                VBox card = loader.load();
                NoteCardController controller = loader.getController();
                controller.setNote(note);
                notesContainer.getChildren().add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (notes.isEmpty()) {
            var emptyText = new Text("Архив пуст.");
            emptyText.setStyle("-fx-fill: #ffffff; -fx-font-size: 16px;");
            notesContainer.getChildren().add(emptyText);
        }
    }

    public void onAddButtonClick(MouseEvent mouseEvent) {
        this.notesContainer.getScene().getWindow().fireEvent(new CustomEvent(CustomEvent.OPEN_ADD_NOTE));
    }
}
