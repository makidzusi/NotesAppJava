package com.stanislav.todoappjava.services;

import com.stanislav.todoappjava.interfaces.IUserService;
import com.stanislav.todoappjava.models.UserModel;
import com.stanislav.todoappjava.repositories.UserRepository;

public class UserService implements IUserService {

    private final UserRepository userRepo;

    public  UserService() {
        this.userRepo = new UserRepository();
    }

    @Override
    public boolean login(String login, String password) {
        return userRepo.validateLogin(login, password);
    }

    @Override
    public boolean register(String login, String password) {
        if (login == null || login.isBlank() || password == null || password.isBlank()) {
            return false;
        }

        if (userRepo.findByLogin(login) != null) {
            return false; // Логин занят
        }

        UserModel user = new UserModel(login, password);
        return userRepo.createUser(user);
    }

    @Override
    public UserModel getUserByLogin(String login) {
        return this.userRepo.findByLogin(login);
    }

    @Override
    public boolean checkUserExists(String login, String password) {
        return this.userRepo.validateLogin(login, password);
    }
}
