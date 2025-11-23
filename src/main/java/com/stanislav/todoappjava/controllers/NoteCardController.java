package com.stanislav.todoappjava.controllers;

import com.stanislav.todoappjava.interfaces.INotesService;
import com.stanislav.todoappjava.services.NotesService;
import com.stanislav.todoappjava.utils.CustomEvent;
import com.stanislav.todoappjava.utils.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import com.stanislav.todoappjava.models.NoteModel;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class NoteCardController {

    @FXML
    public VBox cardContainer;

    @FXML
    private Label titleLabel;

    @FXML
    private Text contentText;

    @FXML
    private Label dateLabel;

    @FXML
    private Button favoriteBtn;

    @FXML
    private Button openBtn;

    private Runnable onFavouriteChanged;

    private final INotesService notesService;

    private NoteModel note;


    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public NoteCardController() {
        this.notesService = new NotesService();
    }

    private void initContextMenu() {
        MenuItem removeItem = new MenuItem("Удалить");
        MenuItem archiveItem = new MenuItem(this.note.isArchived() ? "Ввернуть из архива" : "В архив");
        MenuItem toggleItem = new MenuItem("Переключить избранное");
        final ContextMenu contextMenu = new ContextMenu();

        toggleItem.setOnAction((e) -> this.onToggleFavourite());
        archiveItem.setOnAction((e) -> {
            this.onToggleIsArchived();
        });
        removeItem.setOnAction((e) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Удаление заметки");
            alert.setHeaderText("Вы точно хотите удалить заметку?");
            alert.setContentText(note.getTitle());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                notesService.deleteNotById(this.note.id);
                this.cardContainer.getScene().getWindow().fireEvent(new CustomEvent(CustomEvent.REFRESH_NOTES));
            }
        });

        contextMenu.getItems().addAll(removeItem, archiveItem, toggleItem);


        this.cardContainer.setOnContextMenuRequested(e ->
                contextMenu.show(this.cardContainer, e.getScreenX(), e.getScreenY())
        );

        contextMenu.setAutoHide(true);
    }

    public void setNote(NoteModel note) {
        titleLabel.setText(note.getTitle());
        var content = note.getContent();
        contentText.setText(content.substring(0, Math.min(content.length(), 300)));
        dateLabel.setText(note.getCreatedAt().format(formatter));
        this.note = note;
        Platform.runLater(this::setStyles);
        this.initContextMenu();
    }

    public void setOnFavouriteChanged(Runnable listener) {
        this.onFavouriteChanged = listener;
    }

    public void onOpenNote(MouseEvent mouseEvent) {
        var mainController = SceneManager.getMainController();
        mainController.openNoteInMain(this.note.id);
    }

    public void onToggleFavourite() {
        this.notesService.toggleNoteFavourite(this.note.id, !this.note.isFavourite());
        this.note.setFavourite(!this.note.isFavourite());
        this.setStyles();
        if (onFavouriteChanged != null) {
            onFavouriteChanged.run(); // вызываем колбэк
        }
    }

    public void onToggleIsArchived() {
        this.notesService.toggleNoteIsArchived(this.note.id, !note.isArchived());
        SceneManager.primaryStage.fireEvent(new CustomEvent(CustomEvent.REFRESH_NOTES));
    }

    private void setStyles() {
        if (this.note.isFavourite()) {
            this.favoriteBtn.setTextFill(Paint.valueOf("#ffcc00"));
        } else {
            this.favoriteBtn.setTextFill(Paint.valueOf("#ffffff"));
        }
    }
}
