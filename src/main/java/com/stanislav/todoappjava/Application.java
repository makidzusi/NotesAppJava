package com.stanislav.todoappjava;

import com.stanislav.todoappjava.utils.SceneManager;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        SceneManager.setStage(stage);
        SceneManager.switchTo("/com/stanislav/todoappjava/views/login-view.fxml");
        stage.setMaximized(true);
        stage.setTitle("Notes");
        stage.show();
    }
}
