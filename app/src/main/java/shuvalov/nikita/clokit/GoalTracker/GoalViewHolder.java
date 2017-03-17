package shuvalov.nikita.clokit.goaltracker;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import shuvalov.nikita.clokit.AppUtils;
import shuvalov.nikita.clokit.pojos.Goal;
import shuvalov.nikita.clokit.R;

/**
 * Created by NikitaShuvalov on 3/3/17.
 */

public class GoalViewHolder extends RecyclerView.ViewHolder {
    private TextView mGoalText, mCurrentTime, mEndTime, mSubCatText;
    private ProgressBar mProgressBar;
//    public Button mEditButton;
    public ToggleButton mToggleButton;
//    public Button mRemoveButton;
    public CardView mContainer;


    public GoalViewHolder(View itemView) {
        super(itemView);
        mGoalText = (TextView)itemView.findViewById(R.id.goal_name);
        mCurrentTime =  (TextView)itemView.findViewById(R.id.time_text);
        mEndTime = (TextView)itemView.findViewById(R.id.goal_text);
        mProgressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
//        mEditButton = (Button)itemView.findViewById(R.id.edit_button);
        mToggleButton = (ToggleButton)itemView.findViewById(R.id.clock_it_button);
        mSubCatText = (TextView)itemView.findViewById(R.id.subcat_name);
//        mRemoveButton = (Button)itemView.findViewById(R.id.remove_goal_butt);
        mContainer = (CardView)itemView.findViewById(R.id.card_container);
    }

    public void bindDataToViews(Goal goal){
        mGoalText.setText(goal.getGoalName());
        String subCat = goal.getSubCategory();
        if(subCat!=null){
            mSubCatText.setVisibility(View.VISIBLE);
            mSubCatText.setText(subCat);
        }else{
            mSubCatText.setVisibility(View.GONE);
        }
        long goalMilli = goal.getEndMilli();
        long currentMilli= goal.getCurrentMilli();

        mCurrentTime.setText(AppUtils.getHoursAndMinutes(currentMilli));
        mEndTime.setText(AppUtils.getHoursAndMinutes(goalMilli));

        mProgressBar.setMax(100);

        //A little extra work to change from long to int
        String p =String.valueOf( (currentMilli*100)/goalMilli );
        int progress = Integer.valueOf(p);

        if(progress>100){
            progress=100;
        }
        mProgressBar.setProgress(progress);
    }
}
