package shuvalov.nikita.clokit.todo_list;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NikitaShuvalov on 2/15/18.
 */

public class ToDoListManager {
    private List<ToDoItem> mToDoItems;

    private static ToDoListManager sToDoListManager;

    public ToDoListManager() {
        mToDoItems = new ArrayList<>();
    }

    public static ToDoListManager getInstance() {
        if(sToDoListManager == null){
            sToDoListManager = new ToDoListManager();
        }
        return sToDoListManager;
    }

    public List<ToDoItem> getToDoItems() {
        return mToDoItems;
    }

    public void setToDoItems(List<ToDoItem> toDoItems) {
        mToDoItems = toDoItems;
    }

    public void addToDoItem(ToDoItem item){
        mToDoItems.add(item);
    }
}
