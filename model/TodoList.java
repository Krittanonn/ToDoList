package model;

import java.util.ArrayList;
import java.util.List;

public class TodoList {
    private String title;
    private List<Task> tasks = new ArrayList<>();

    public TodoList(String title) {
        this.title = title;
    }

    public String getTitle() { return title; }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
