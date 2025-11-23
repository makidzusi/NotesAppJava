package com.stanislav.todoappjava.controllers;

import com.stanislav.todoappjava.interfaces.IUserService;
import com.stanislav.todoappjava.services.UserService;
import com.stanislav.todoappjava.utils.SceneManager;
import com.stanislav.todoappjava.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

public class LoginController {
    @FXML
    public TextField usernameField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public Label errorLabel;

    @FXML
    public Hyperlink hyperlink;

    @FXML
    public Button actionButton;

    @FXML
    public Label formLabel;

    private final IUserService userService;

    public LoginController() {
        this.userService = new UserService();
    }

    private boolean isLoginMode = true;

    @FXML
    private void initialize() {
        usernameField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                errorLabel.setVisible(false);
            }
        });
        passwordField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                errorLabel.setVisible(false);
            }
        });
    }

    public void onLoginClick(ActionEvent actionEvent) {
        String login = usernameField.getText();
        String password = passwordField.getText();

        if (!this.isLoginMode) {
            var registerResult = this.userService.register(login, password);
            if (registerResult) {
                var user = this.userService.getUserByLogin(login);
                SessionManager.setCurrentUser(user);
                SceneManager.switchTo("/com/stanislav/todoappjava/views/main-view.fxml");
            } else {
                errorLabel.setText("Такой пользователь уже существует");
            }
            return;
        }

        if (this.userService.checkUserExists(login, password)) {
            var user = this.userService.getUserByLogin(login);
            SessionManager.setCurrentUser(user);
            SceneManager.switchTo("/com/stanislav/todoappjava/views/main-view.fxml");
        } else {
            errorLabel.setVisible(true);
            errorLabel.setText("Неверный логин или пароль");
        }
    }

    public void onRegisterClick(ActionEvent actionEvent) {
        if (this.isLoginMode) {
            this.hyperlink.setText("Уже есть аккаунт? Войти.");
            this.actionButton.setText("Зарегестрироваться");
            this.isLoginMode = false;
            this.formLabel.setText("Регистрация");
        } else {
            this.hyperlink.setText("Нет аккаунта? Создать");
            this.actionButton.setText("Войти");
            this.formLabel.setText("Вход");
            this.isLoginMode = true;
        }

    }
}
