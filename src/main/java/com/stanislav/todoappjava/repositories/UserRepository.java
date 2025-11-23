package com.stanislav.todoappjava.repositories;

import com.stanislav.todoappjava.datasource.Datasource;
import com.stanislav.todoappjava.models.UserModel;

import java.sql.*;

public class UserRepository {

    private static final String DB_URL = Datasource.URL;

    public UserRepository() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS users (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        login TEXT UNIQUE NOT NULL,
                        password TEXT NOT NULL
                    );
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean createUser(UserModel user) {
        String sql = "INSERT INTO users (login, password) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getLogin());
            pstmt.setString(2, user.getPassword());
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            return false; // логин уже существует
        }
    }

    public UserModel findByLogin(String login) {
        String sql = "SELECT * FROM users WHERE login = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new UserModel(rs.getInt("id"), rs.getString("login"), rs.getString("password"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // не найден
    }

    public boolean validateLogin(String login, String password) {
        String sql = "SELECT * FROM users WHERE login = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, login);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
