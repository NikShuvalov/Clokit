package shuvalov.nikita.clokit.lifetime_results;

import android.support.v7.widget.CardView;
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
    private TextView mMainText, mTimeText, mRankText;
    public CardView mContainerCard;

    public LifetimeHeaderViewHolder(View itemView) {
        super(itemView);
        mMainText = (TextView)itemView.findViewById(R.id.main_text);
        mTimeText = (TextView)itemView.findViewById(R.id.time_text);
        mContainerCard = (CardView)itemView.findViewById(R.id.container_card);
        mRankText = (TextView)itemView.findViewById(R.id.ranking_text);
    }

    public void bindDataToViews(Goal goal){
        long hoursWorked = AppUtils.getHoursOfWork(goal.getCurrentMilli());
        String mainString = goal.getGoalName();
        mMainText.setText(mainString);
        mRankText.setText(AppUtils.identifyRank(hoursWorked));
        mTimeText.setText(AppUtils.getHoursAndMinutes(goal.getCurrentMilli()));
    }
}
