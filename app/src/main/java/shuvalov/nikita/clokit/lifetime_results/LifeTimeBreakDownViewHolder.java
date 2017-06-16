package shuvalov.nikita.clokit.lifetime_results;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

import shuvalov.nikita.clokit.AppUtils;
import shuvalov.nikita.clokit.R;
import shuvalov.nikita.clokit.pojos.Goal;

/**
 * Created by NikitaShuvalov on 6/15/17.
 */

public class LifeTimeBreakDownViewHolder extends RecyclerView.ViewHolder {
    public static final String UNDEFINED = "Undefined";
    private TextView mCatText, mTimeText;


    public LifeTimeBreakDownViewHolder(View itemView) {
        super(itemView);
        mCatText = (TextView)itemView.findViewById(R.id.sub_cat_text);
        mTimeText = (TextView)itemView.findViewById(R.id.time_text);
    }

    public void bindDataToViews(String subCat, long milli){
        String subcategory = subCat;
        if(subcategory== null || subcategory.isEmpty()){
            mCatText.setText(UNDEFINED);
        }else {
            mCatText.setText(subCat);
        }
        mTimeText.setText(AppUtils.getHoursAndMinutes(milli));
    }
}
