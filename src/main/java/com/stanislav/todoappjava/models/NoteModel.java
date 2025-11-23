package com.stanislav.todoappjava.models;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class NoteModel {
    public final Integer id;
    private final Integer userId;
    private final LocalDateTime createdAt;
    private String title;
    private String content;
    private boolean isFavourite;
    private boolean isArchived;

    public NoteModel(Integer id, String title, String content, String createdAt, Integer userId, boolean isFavourite, boolean isArchived) {
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.ofInstant(Instant.parse(createdAt), ZoneId.systemDefault());
        this.userId = userId;
        this.id = id;
        this.isFavourite = isFavourite;
        this.isArchived = isArchived;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String content) {
        this.title = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String _content) {
        this.content = _content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getId() {
        return id;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setIsArchived(boolean state) {
        isArchived = state;
    }
}
