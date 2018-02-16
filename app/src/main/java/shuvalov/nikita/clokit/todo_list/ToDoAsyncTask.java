package shuvalov.nikita.clokit.todo_list;

import android.content.Context;
import android.os.AsyncTask;

import shuvalov.nikita.clokit.GoalSQLHelper;

/**
 * Created by NikitaShuvalov on 2/15/18.
 */

public class ToDoAsyncTask extends AsyncTask<Context, Void,Void> {

    @Override
    protected Void doInBackground(Context... context) {
        ToDoListManager.getInstance().setToDoItems(GoalSQLHelper.getInstance(context[0]).getToDoList());
        return null;
    }

}
