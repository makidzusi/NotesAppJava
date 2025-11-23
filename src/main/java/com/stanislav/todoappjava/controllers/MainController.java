package com.stanislav.todoappjava.controllers;

import com.stanislav.todoappjava.utils.CustomEvent;
import com.stanislav.todoappjava.utils.SceneManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;

import java.io.IOException;

public class MainController {

    @FXML
    private StackPane contentArea;

    @FXML
    private Button notesButton, favoritesButton, trashButton, logoutButton;

    @FXML
    private void initialize() {
        // Загружаем дефолтный контент
        SceneManager.setMainController(this);
        showNotes();
        Platform.runLater(() -> {
            this.contentArea.getScene().getWindow().addEventHandler(
                    CustomEvent.OPEN_ADD_NOTE,
                    e -> loadContent("/com/stanislav/todoappjava/views/add-note-view.fxml")
            );
            this.contentArea.getScene().getWindow().addEventHandler(
                    CustomEvent.OPEN_NOTES_VIEW,
                    e -> this.showNotes()
            );

            this.contentArea.getScene().getWindow().addEventHandler(
                    CustomEvent.OPEN_NOTE_VIEW,
                    e -> loadContent("/com/stanislav/todoappjava/views/note-view.fxml")
            );
        });
    }

    @FXML
    public void showNotes() {
        loadContent("/com/stanislav/todoappjava/views/notes-view.fxml");
    }

    @FXML
    private void showFavorites() {
        loadContent("/com/stanislav/todoappjava/views/favourites-view.fxml");
    }

    @FXML
    private void showTrash() {
        loadContent("/com/stanislav/todoappjava/views/archive-view.fxml");
    }


    @FXML
    private void logout() {
        // Переход на экран логина
        SceneManager.switchTo("/com/stanislav/todoappjava/views/login-view.fxml");
    }


    public void openNoteInMain(int noteId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/stanislav/todoappjava/views/note-view.fxml"));
            Parent noteView = loader.load();

            NoteController controller = loader.getController();
            controller.loadNoteById(noteId);

            contentArea.getChildren().setAll(noteView);  // заменить контент
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadContent(String fxmlPath) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showGraph(ActionEvent actionEvent) {
        loadContent("/com/stanislav/todoappjava/views/graph-view.fxml");
    }
}
