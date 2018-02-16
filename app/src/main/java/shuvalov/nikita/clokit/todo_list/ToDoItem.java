package shuvalov.nikita.clokit.todo_list;

/**
 * Created by NikitaShuvalov on 2/15/18.
 */

public class ToDoItem {
    private String name, description;
    private long created, due;
    private boolean isComplete;

    public ToDoItem(String name, String description, long created, long due, boolean isComplete) {
        this.name = name;
        this.created = created;
        this.due = due;
        this.isComplete = isComplete;
    }

    public ToDoItem(String name, String description, long created, long due) {
        this(name, description, created, due, false);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getDue() {
        return due;
    }

    public void setDue(long due) {
        this.due = due;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
