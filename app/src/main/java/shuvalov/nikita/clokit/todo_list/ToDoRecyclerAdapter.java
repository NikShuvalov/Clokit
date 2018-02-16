package shuvalov.nikita.clokit.todo_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.List;

import shuvalov.nikita.clokit.GoalSQLHelper;
import shuvalov.nikita.clokit.R;

/**
 * Created by NikitaShuvalov on 2/15/18.
 */

public class ToDoRecyclerAdapter extends RecyclerView.Adapter<ToDoViewHolder> {

    private List<ToDoItem> mToDoItems;

    public ToDoRecyclerAdapter() {
        mToDoItems = ToDoListManager.getInstance().getToDoItems();
    }

    @Override
    public ToDoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ToDoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_todo, parent, false));
    }

    @Override
    public void onBindViewHolder(final ToDoViewHolder holder, int position) {
        final ToDoItem toDoItem = mToDoItems.get(position);

        holder.setToDoItem(toDoItem);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                toDoItem.setComplete(b);
                holder.setToDoItem(toDoItem);
                GoalSQLHelper.getInstance(compoundButton.getContext()).updateToDoItem(toDoItem);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ToDoListManager.getInstance().getToDoItems().size();
    }

    public void refreshItems(){
        mToDoItems = ToDoListManager.getInstance().getToDoItems();
        notifyDataSetChanged();
    }
}
