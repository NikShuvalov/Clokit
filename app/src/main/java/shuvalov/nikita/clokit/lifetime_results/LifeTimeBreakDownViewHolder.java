package shuvalov.nikita.clokit.lifetime_results;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import shuvalov.nikita.clokit.AppUtils;
import shuvalov.nikita.clokit.R;

/**
 * Created by NikitaShuvalov on 6/15/17.
 */

public class LifeTimeBreakDownViewHolder extends RecyclerView.ViewHolder {
    public static final String UNDEFINED = "Undefined";
    private TextView mCatText, mTimeText, mPercentText;


    public LifeTimeBreakDownViewHolder(View itemView) {
        super(itemView);
        mCatText = (TextView)itemView.findViewById(R.id.sub_cat_text);
        mTimeText = (TextView)itemView.findViewById(R.id.time_text);
        mPercentText = (TextView)itemView.findViewById(R.id.time_percentage_text);
    }

    public void bindDataToViews(String subCat, long milli, long total){
        Log.d("Boop", "bindDataToViews: "+ total);
        if(subCat== null || subCat.isEmpty()){
            mCatText.setText(UNDEFINED);
        }else {
            mCatText.setText(subCat);
        }
        mTimeText.setText(AppUtils.getHoursAndMinutes(milli));
        double seconds = milli/1000;
        double totalSeconds= total/1000;
        double percent = 0;
        if(total>0) {
            percent = (int)((seconds / totalSeconds)*10000);
            percent = percent/100;
        }
        String percentageText = percent + " %";
        mPercentText.setText(percentageText);
    }
}
