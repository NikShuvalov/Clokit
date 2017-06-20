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
        mGoalText.setText(goal.getGoalName());
        long hoursWorked = AppUtils.getHoursOfWork(goal.getCurrentMilli());
        mRankingText.setText(AppUtils.identifyRank(hoursWorked));
        mTimeText.setText(AppUtils.getHoursAndMinutes(goal.getCurrentMilli()));
    }
}
