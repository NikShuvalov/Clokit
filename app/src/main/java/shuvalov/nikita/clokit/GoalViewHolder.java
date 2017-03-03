package shuvalov.nikita.clokit;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.concurrent.TimeUnit;

/**
 * Created by NikitaShuvalov on 3/3/17.
 */

public class GoalViewHolder extends RecyclerView.ViewHolder {
    private TextView mGoalText, mBeginTime, mCurrentTime, mEndTime;
    private ProgressBar mProgressBar;
    public Button mEditButton;
    public ToggleButton mToggleButton;

    public GoalViewHolder(View itemView) {
        super(itemView);
        mGoalText = (TextView)itemView.findViewById(R.id.goal_name);
        mBeginTime = (TextView)itemView.findViewById(R.id.begin_text);
        mCurrentTime =  (TextView)itemView.findViewById(R.id.time_text);
        mEndTime = (TextView)itemView.findViewById(R.id.goal_text);
        mProgressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
        mEditButton = (Button)itemView.findViewById(R.id.edit_button);
        mToggleButton = (ToggleButton)itemView.findViewById(R.id.clock_it_button);
    }

    public void bindDataToViews(Goal goal){
        mGoalText.setText(goal.getGoalName());
        mBeginTime.setText("0hr");
        long currentMilli= goal.getCurrentMilli();
//        String currentText = String.format("%02d:%02d:%02d",
//                TimeUnit.MILLISECONDS.toHours(currentMilli),
//                TimeUnit.MILLISECONDS.toMinutes(currentMilli),
//                TimeUnit.MILLISECONDS.toHours(currentMilli)
//                );
        //ToDo: Create a util that changes milli to hours:minutes.


    }
}
