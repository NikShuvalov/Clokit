package shuvalov.nikita.clokit.lifetime_results;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import shuvalov.nikita.clokit.AppConstants;
import shuvalov.nikita.clokit.AppUtils;
import shuvalov.nikita.clokit.pojos.Goal;
import shuvalov.nikita.clokit.R;

import static android.content.ContentValues.TAG;

/**
 * Created by NikitaShuvalov on 3/6/17.
 */

public class LifetimeViewHolder extends RecyclerView.ViewHolder {
    private TextView mGoalText, mTimeText, mRankingText;
    public CardView mContainerCard;

    public LifetimeViewHolder(View itemView) {
        super(itemView);
        mGoalText = (TextView)itemView.findViewById(R.id.goal_name_text);
        mTimeText = (TextView)itemView.findViewById(R.id.time_text);
        mRankingText = (TextView) itemView.findViewById(R.id.ranking_text);
        mContainerCard = (CardView)itemView.findViewById(R.id.container_card);
    }

    public void bindDataToViews(Goal goal){
        Log.d("VIEWHOLDER", "bindDataToViews: "+ goal.getGoalName());
        mGoalText.setText(goal.getGoalName());
        long hoursWorked = AppUtils.getHoursOfWork(goal.getCurrentMilli());
        mRankingText.setText(identifyRank(hoursWorked));
        mTimeText.setText(AppUtils.getHoursAndMinutes(goal.getCurrentMilli()));
    }

    private String identifyRank(long hours){
        hours *= 10; //ToDo: Remove later if wanting more serious values, this is just for testing porpoises! DOLFINS! BLOW-UP THE OCEAN!
        if(hours>10000){
            return "God";
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
