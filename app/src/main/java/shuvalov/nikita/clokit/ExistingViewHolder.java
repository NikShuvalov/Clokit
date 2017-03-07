package shuvalov.nikita.clokit;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import shuvalov.nikita.clokit.pojos.Goal;

/**
 * Created by NikitaShuvalov on 3/7/17.
 */

public class ExistingViewHolder extends RecyclerView.ViewHolder {
    private TextView mGoalName, mTimeView;
    public CardView mCardView;

    public ExistingViewHolder(View itemView) {
        super(itemView);
        mGoalName = (TextView) itemView.findViewById(R.id.goal_name_text);
        mTimeView = (TextView)itemView.findViewById(R.id.total_time_text);
        mCardView = (CardView)itemView.findViewById(R.id.goal_card);
    }
    public void bindDataToViews(Goal goal){
        mGoalName.setText(goal.getGoalName());
        String timeText = AppUtils.getLifetimeSummaryText(goal.getCurrentMilli());
        mTimeView.setText(timeText);
    }

    public void unSelectColor() {
        mCardView.setCardBackgroundColor(Color.argb(255, 255, 255, 255));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCardView.setElevation(2f);
        }
    }

    public void selectColor(){
        mCardView.setCardBackgroundColor(Color.argb(100,150,229,255));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCardView.setElevation(10f);
        }
    }
}
