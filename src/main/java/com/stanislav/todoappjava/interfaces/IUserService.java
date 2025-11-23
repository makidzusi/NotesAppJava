package com.stanislav.todoappjava.interfaces;

import com.stanislav.todoappjava.models.UserModel;

public interface IUserService {
    boolean login(String login, String password);

    boolean register(String login, String password);

    UserModel getUserByLogin(String login);

    boolean checkUserExists(String login, String password);
}
