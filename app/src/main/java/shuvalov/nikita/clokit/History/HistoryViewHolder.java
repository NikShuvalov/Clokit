package shuvalov.nikita.clokit.history;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import shuvalov.nikita.clokit.AppUtils;
import shuvalov.nikita.clokit.pojos.Week;
import shuvalov.nikita.clokit.R;

/**
 * Created by NikitaShuvalov on 3/6/17.
 */

public class HistoryViewHolder extends RecyclerView.ViewHolder {
    private TextView mStartText;
    public CardView mCardView;
//    private TextView mEndText;

    public HistoryViewHolder(View itemView) {
        super(itemView);
        mStartText = (TextView)itemView.findViewById(R.id.week_start_text);
        mCardView = (CardView)itemView.findViewById(R.id.week_card);

//        mEndText = (TextView)itemView.findViewById(R.id.week_end_text);
    }

    public void bindDataToViews(Week week){
        String startDateText = "Week of: " + AppUtils.getDate(week.getStartTime());
        mStartText.setText(startDateText);
//        mEndText.setText(AppUtils.getDate(week.getEndTime()));
    }
}
