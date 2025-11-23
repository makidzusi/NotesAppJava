package com.stanislav.todoappjava.utils;

import com.stanislav.todoappjava.controllers.GraphController;
import javafx.application.Platform;

public class JsBridge {
    private final GraphController controller;

    public JsBridge(GraphController controller) {
        this.controller = controller;
    }

    public int openNote(String noteId) {
        System.out.println("opened note tipa");
        Platform.runLater(() -> controller.openNoteById(Integer.parseInt(noteId)));
        return 10;
    }
}
