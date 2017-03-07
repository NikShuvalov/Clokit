package shuvalov.nikita.clokit.History;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import shuvalov.nikita.clokit.POJOs.Week;
import shuvalov.nikita.clokit.R;

/**
 * Created by NikitaShuvalov on 3/6/17.
 */

public class HistoryViewHolder extends RecyclerView.ViewHolder {
    TextView mStartText, mEndText;

    public HistoryViewHolder(View itemView) {
        super(itemView);
        mStartText = (TextView)itemView.findViewById(R.id.week_start_text);
        mEndText = (TextView)itemView.findViewById(R.id.week_end_text);
    }

    public void bindDataToViews(Week week){
        //ToDo: Create Util that returns date from week start and end time in MM/DD/YYYY format.
    }
}
