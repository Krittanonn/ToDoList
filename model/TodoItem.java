package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TodoItem {
    private String title;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;
    private boolean completed;

    public TodoItem(String title, String category, LocalDateTime deadline) {
        this.title = title;
        this.category = category;
        this.createdAt = LocalDateTime.now();
        this.deadline = deadline;
        this.completed = false;
    }

    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getDeadline() { return deadline; }
    public boolean isCompleted() { return completed; }

    public void toggleCompleted() { this.completed = !this.completed; }

    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("[%s] %s (%s) - Due: %s", completed ? "X" : " ", title, category, deadline.format(formatter));
    }
}