package controller;

import model.Task;
import model.TodoList;

import java.util.ArrayList;
import java.util.List;

public class TodoListController {
    private List<TodoList> lists = new ArrayList<>();

    public void addList(String name) {
        lists.add(new TodoList(name));
    }

    public List<TodoList> getAllLists() {
        return lists;
    }

    public void addTaskToList(int index, Task task) {
        if (index >= 0 && index < lists.size()) {
            lists.get(index).addTask(task);
        }
    }

    public TodoList getList(int index) {
        return lists.get(index);
    }
}
