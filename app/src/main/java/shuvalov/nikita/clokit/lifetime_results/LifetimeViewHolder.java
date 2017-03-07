package shuvalov.nikita.clokit.lifetime_results;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import shuvalov.nikita.clokit.AppUtils;
import shuvalov.nikita.clokit.pojos.Goal;
import shuvalov.nikita.clokit.R;

/**
 * Created by NikitaShuvalov on 3/6/17.
 */

public class LifetimeViewHolder extends RecyclerView.ViewHolder {
    private TextView mGoalText, mTimeText;

    public LifetimeViewHolder(View itemView) {
        super(itemView);
        mGoalText = (TextView)itemView.findViewById(R.id.goal_name_text);
        mTimeText = (TextView)itemView.findViewById(R.id.time_text);
    }

    public void bindDataToViews(Goal goal){
        Log.d("VIEWHOLDER", "bindDataToViews: "+ goal.getGoalName());
        mGoalText.setText(goal.getGoalName());
        mTimeText.setText(AppUtils.getHoursAndMinutes(goal.getCurrentMilli()));
    }
}
