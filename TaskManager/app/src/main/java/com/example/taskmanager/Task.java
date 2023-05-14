package com.example.taskmanager;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private long id;
    private String description;
    private long dueDate;
    private boolean isCompleted;
    private String Category;
    private String priority;
    private String notes;
    private List<Task> subtasks;

    // Constructor con id
    public Task(String description, long dueDate, String category, String priority,String notes, ArrayList subtasks) {
        this.id = id;
        this.description = description;
        this.dueDate = dueDate;
        this.isCompleted = isCompleted;
        this.Category = category;
        this.priority = priority;
        this.notes = "";
        this.subtasks = new ArrayList<>();
    }

    // Constructor sin id (usado en MainActivity)
    public Task(String description, long dueDate, boolean isCompleted, String category, String priority,String notes, ArrayList subtasks) {
        this.id = System.currentTimeMillis(); // Usa la marca de tiempo actual como id único
        this.description = description;
        this.dueDate = dueDate;
        this.isCompleted = isCompleted;
        this.Category = category;
        this.priority = priority;
        this.notes = "";
        this.subtasks = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}