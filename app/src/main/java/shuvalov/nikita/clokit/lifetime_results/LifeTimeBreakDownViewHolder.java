package shuvalov.nikita.clokit.lifetime_results;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import shuvalov.nikita.clokit.AppUtils;
import shuvalov.nikita.clokit.R;

import static android.view.View.GONE;

/**
 * Created by NikitaShuvalov on 6/15/17.
 */

public class LifeTimeBreakDownViewHolder extends RecyclerView.ViewHolder{
    public static final String UNDEFINED = "Undefined";
    private TextView mCatText, mTimeText, mPercentText;
    public CheckBox mCheckBox;
    private boolean mChecked;


    public LifeTimeBreakDownViewHolder(View itemView) {
        super(itemView);
        mCatText = (TextView)itemView.findViewById(R.id.sub_cat_text);
        mTimeText = (TextView)itemView.findViewById(R.id.time_text);
        mPercentText = (TextView)itemView.findViewById(R.id.time_percentage_text);
        mCheckBox = (CheckBox)itemView.findViewById(R.id.checkbox);
    }

    public void bindDataToViews(String subCat, long milli, long total){
        //ToDo: Color highlight selected rows?
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

    public void emptyListPrompt(){
        String text = "There's no entries to show yet; go work on your goals!";
        mCatText.setText(text);
        mCatText.setTextSize(20f);
        mTimeText.setVisibility(GONE);
        mPercentText.setVisibility(GONE);
    }

    public void setChecked(boolean b){
        mChecked =b;
    }

    public boolean isChecked(){
        return mChecked;
    }
}
