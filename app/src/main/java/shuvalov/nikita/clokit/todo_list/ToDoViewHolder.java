package shuvalov.nikita.clokit.todo_list;

import android.graphics.Paint;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import shuvalov.nikita.clokit.R;

/**
 * Created by NikitaShuvalov on 2/15/18.
 */

public class ToDoViewHolder extends RecyclerView.ViewHolder {
    public AppCompatCheckBox mCheckBox;
    public AppCompatTextView mTextView;

    public ToDoViewHolder(View itemView) {
        super(itemView);
        mTextView = (AppCompatTextView)itemView.findViewById(R.id.description_text);
        mCheckBox = (AppCompatCheckBox)itemView.findViewById(R.id.checkbox);
    }

    public void setToDoItem(ToDoItem td){
        mCheckBox.setChecked(td.isComplete());
        mTextView.setText(td.getName());
        if(td.isComplete()) {
            mTextView.setPaintFlags(mTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            mTextView.setPaintFlags(mTextView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

}
