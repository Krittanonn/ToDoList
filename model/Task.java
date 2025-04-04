package model;

import java.time.LocalDateTime;

public class Task {
    private String name;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;

    public Task(String name, String category, LocalDateTime deadline) {
        this.name = name;
        this.category = category;
        this.createdAt = LocalDateTime.now();
        this.deadline = deadline;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getDeadline() { return deadline; }

    @Override
    public String toString() {
        return name + " [" + category + "] - Due: " + deadline.toString();
    }
}
