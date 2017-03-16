package shuvalov.nikita.clokit.lifetime_results;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import shuvalov.nikita.clokit.AppUtils;
import shuvalov.nikita.clokit.R;
import shuvalov.nikita.clokit.pojos.Goal;

/**
 * Created by NikitaShuvalov on 3/16/17.
 */

public class LifetimeHeaderViewHolder extends RecyclerView.ViewHolder {
    private TextView mMainText, mTimeText;

    public LifetimeHeaderViewHolder(View itemView) {
        super(itemView);
        mMainText = (TextView)itemView.findViewById(R.id.main_text);
        mTimeText = (TextView)itemView.findViewById(R.id.time_text);
    }

    public void bindDataToViews(Goal goal){
        Log.d("VIEWHOLDER", "bindDataToViews: "+ goal.getGoalName());
        long hoursWorked = AppUtils.getHoursOfWork(goal.getCurrentMilli());
        String mainString = goal.getGoalName() + " " + identifyRank(hoursWorked);
        mMainText.setText(mainString);
        mTimeText.setText(AppUtils.getHoursAndMinutes(goal.getCurrentMilli()));
    }

    private String identifyRank(long hours){
        hours *= 10; //ToDo: Remove later if wanting more serious values, this is just for testing porpoises! DOLFINS! BLOW-UP THE OCEAN!
        if(hours>10000){
            return "Godly";
        }else if(hours>7500){
            return "Transcendent";
        }else if (hours>5000){
            return "GrandMaster";
        }else if (hours>2500){
            return "Master";
        }else if (hours>1000){
            return "Professional";
        }else if (hours>750){
            return "Expert";
        }else if (hours>500){
            return "Journeyman";
        }else if (hours >250){
            return "Apprentice";
        }else if (hours>100){
            return "Novice";
        }else if (hours>50){
            return "Beginner";
        }else if (hours>10){
            return "Initiate";
        }else{
            return "Newbie";
        }
    }
}
