package shuvalov.nikita.clokit;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import shuvalov.nikita.clokit.POJOs.Goal;

/**
 * Created by NikitaShuvalov on 3/3/17.
 */

public class GoalViewHolder extends RecyclerView.ViewHolder {
    private TextView mGoalText, mCurrentTime, mEndTime;
    private ProgressBar mProgressBar;
    public Button mEditButton;
    public ToggleButton mToggleButton;

    public GoalViewHolder(View itemView) {
        super(itemView);
        mGoalText = (TextView)itemView.findViewById(R.id.goal_name);
        mCurrentTime =  (TextView)itemView.findViewById(R.id.time_text);
        mEndTime = (TextView)itemView.findViewById(R.id.goal_text);
        mProgressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
        mEditButton = (Button)itemView.findViewById(R.id.edit_button);
        mToggleButton = (ToggleButton)itemView.findViewById(R.id.clock_it_button);
    }

    public void bindDataToViews(Goal goal){
        mGoalText.setText(goal.getGoalName());
        long goalMilli = goal.getEndMilli();
        long currentMilli= goal.getCurrentMilli();

        mCurrentTime.setText(AppUtils.getHoursAndMinutes(currentMilli));
        mEndTime.setText(AppUtils.getHoursAndMinutes(goalMilli));

        mProgressBar.setMax(100);

        //A little extra work to change from long to int
        String p =String.valueOf( (currentMilli*100)/goalMilli );
        int progress = Integer.valueOf(p);

        mProgressBar.setProgress(progress);
    }
}
