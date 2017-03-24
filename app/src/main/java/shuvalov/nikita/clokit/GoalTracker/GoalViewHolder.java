package shuvalov.nikita.clokit.goaltracker;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import shuvalov.nikita.clokit.AppUtils;
import shuvalov.nikita.clokit.pojos.Goal;
import shuvalov.nikita.clokit.R;

import static android.content.ContentValues.TAG;

/**
 * Created by NikitaShuvalov on 3/3/17.
 */

public class GoalViewHolder extends RecyclerView.ViewHolder {
    private TextView mGoalText, mCurrentTime, mEndTime, mSubCatText;
    public ImageView mEditButton;
    private ProgressBar mProgressBar;
    public ToggleButton mToggleButton;
    public Button mRemoveButton;
    public CardView mContainer;


    public GoalViewHolder(View itemView) {
        super(itemView);
        mGoalText = (TextView)itemView.findViewById(R.id.goal_name);
        mCurrentTime =  (TextView)itemView.findViewById(R.id.time_text);
        mEndTime = (TextView)itemView.findViewById(R.id.goal_text);
        mProgressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
        mEditButton = (ImageView) itemView.findViewById(R.id.goal_edit_button);
        mToggleButton = (ToggleButton) itemView.findViewById(R.id.clock_it_button);
        mSubCatText = (TextView)itemView.findViewById(R.id.subcat_name);
        mRemoveButton = (Button)itemView.findViewById(R.id.remove_goal_butt);
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
        updateProgressBar(currentMilli, goalMilli);
    }

    private void updateProgressBar(long currentMilli, long goalMilli){
        mProgressBar.setMax(1000);

        //A little extra work to change from long to int
        String p =String.valueOf( (currentMilli*1000)/goalMilli );
        int progress = Integer.valueOf(p);
        if(progress>1000){
            progress=1000;
        }
        Log.d(TAG, "updateProgressBar: "+progress);
        if(progress==0){ //Apparently progressbars take values between 0 and the set maxValue. Setting progress = 0 results in no changes which screws up my recycler.
            progress=1;
        }
        mProgressBar.setProgress(progress);
    }
}
