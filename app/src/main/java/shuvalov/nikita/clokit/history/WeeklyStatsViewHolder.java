package shuvalov.nikita.clokit.history;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import shuvalov.nikita.clokit.AppUtils;
import shuvalov.nikita.clokit.R;
import shuvalov.nikita.clokit.pojos.Goal;

/**
 * Created by NikitaShuvalov on 6/13/17.
 */

public class WeeklyStatsViewHolder extends RecyclerView.ViewHolder {
    private TextView mGoalText, mTimeText, mSubCatText;
    public ImageView mMarker;

    public WeeklyStatsViewHolder(View itemView) {
        super(itemView);
        mGoalText = (TextView)itemView.findViewById(R.id.goal_text);
        mTimeText = (TextView)itemView.findViewById(R.id.time_text);
        mMarker = (ImageView)itemView.findViewById(R.id.color_marker);
        mSubCatText = (TextView)itemView.findViewById(R.id.sub_cat_text);
    }

    public void bindDataToViews(Goal goal,int color){
        mGoalText.setText(goal.getGoalName());
        mTimeText.setText(AppUtils.getHoursAndMinutes(goal.getCurrentMilli()));
        mSubCatText.setText(goal.getSubCategory());
        mMarker.setBackgroundColor(color);
    }


}
