package com.stanislav.todoappjava.repositories;

import com.stanislav.todoappjava.datasource.Datasource;
import com.stanislav.todoappjava.models.NoteModel;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class NotesRepository {
    private static final String DB_URL = Datasource.URL;

    public NotesRepository() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS notes (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        title TEXT NOT NULL,
                        content TEXT NOT NULL,
                        is_favourite INTEGER DEFAULT 0,
                        is_archived INTEGER DEFAULT 0,
                        created_at TEXT NOT NULL,
                        user_id INTEGER NOT NULL
                    );
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public NoteModel addNote(String title, String content, int userId) {
        String sql = "INSERT INTO notes(title, content, created_at, user_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            var nowTime = Instant.now().toString();

            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setString(3, nowTime);
            pstmt.setInt(4, userId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating note failed, no rows affected.");
            }

            // Получаем ID созданной записи
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new NoteModel(generatedKeys.getInt(1), // ID заметки
                            title, content, nowTime, userId, false, false);
                } else {
                    throw new SQLException("Creating note failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public NoteModel getNoteById(Integer noteID) {
        String sql = "SELECT * FROM notes WHERE id = ?";
        NoteModel note = null;

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, noteID);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                note = new NoteModel(rs.getInt("id"), rs.getString("title"), rs.getString("content"), rs.getString("created_at"), rs.getInt("user_id"), rs.getInt("is_favourite") == 1, rs.getInt("is_archived") == 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return note;

    }

    public List<NoteModel> getAllUserNotes(int userId) {
        List<NoteModel> notes = new ArrayList<>();

        String sql = "SELECT * FROM notes WHERE user_id = ? ORDER BY created_at DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notes.add(new NoteModel(rs.getInt("id"), rs.getString("title"), rs.getString("content"), rs.getString("created_at"), rs.getInt("user_id"), rs.getInt("is_favourite") == 1, rs.getInt("is_archived") == 1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notes;
    }

    public void deleteNote(int id) {
        String sql = "DELETE FROM notes WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Note deleted: " + id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void toggleFavourite(int noteId, boolean state) {
        String sql = "UPDATE notes SET is_favourite = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, state ? 1 : 0);
            stmt.setInt(2, noteId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void toggleIsArchived(int noteId, boolean state) {
        String sql = "UPDATE notes SET is_archived = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, state ? 1 : 0);
            stmt.setInt(2, noteId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateNoteContentById(int id, String title, String content) {
        String sql = "UPDATE notes SET title = ?, content = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setInt(3, id);

            int updatedRows = pstmt.executeUpdate();
            if (updatedRows > 0) {
                System.out.println("Заметка обновлена успешно");
            } else {
                System.out.println("Заметка с id=" + id + " не найдена");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
