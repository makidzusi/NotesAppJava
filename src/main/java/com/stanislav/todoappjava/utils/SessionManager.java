package com.stanislav.todoappjava.utils;

import com.stanislav.todoappjava.models.UserModel;

public class SessionManager {

    private static UserModel currentUser;

    // Сохранить пользователя
    public static void setCurrentUser(UserModel user) {
        currentUser = user;
    }

    // Получить пользователя
    public static UserModel getCurrentUser() {
        return currentUser;
    }

    // Проверить авторизацию
    public static boolean isAuthenticated() {
        return currentUser != null;
    }

    // Очистить сессию (например при выходе)
    public static void logout() {
        currentUser = null;
    }
}
