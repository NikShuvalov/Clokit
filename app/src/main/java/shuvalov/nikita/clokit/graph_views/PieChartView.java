package shuvalov.nikita.clokit.graph_views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import java.util.ArrayList;

import shuvalov.nikita.clokit.pojos.Goal;

/**
 * Created by NikitaShuvalov on 6/13/17.
 */

public class PieChartView extends View {
    private ArrayList<Goal> mGoals;
    private int mTotalClockedTime;
    private RectF mCircle;
    private Paint mLinePaint, mUnclokedPaint;
    private ArrayList<Paint> mColorPaints;
    private boolean mUseUnclockedTime;

    public static final int TOTAL_WEEK_TIME = 604800000;

    public PieChartView(Context context, ArrayList<Goal> goals) {
        super(context);
        mGoals = goals;
        mUseUnclockedTime = false;
        mTotalClockedTime = 0;
        for(Goal g: goals){
            mTotalClockedTime +=g.getCurrentMilli();
        }
        mLinePaint = new Paint();
        mLinePaint.setColor(Color.BLACK);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(4f);

        mUnclokedPaint = new Paint();
        mUnclokedPaint.setColor(Color.LTGRAY);
        mUnclokedPaint.setStyle(Paint.Style.FILL);

        mColorPaints = new ArrayList<>();
        mCircle = new RectF();
        prepColors();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        float centerX = (left + right) /2;
        float centerY = (top + bottom) / 2;
        float width = right - left;
        float height = bottom - top;
        float radius = width > height ?
                height * .45f :
                width*.45f;
        mCircle.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float startAngle = 0;
        for(int i = 0; i < mGoals.size(); i ++){
            Goal g = mGoals.get(i);
            float arc = mUseUnclockedTime ? ((float)g.getCurrentMilli()/TOTAL_WEEK_TIME) * 360f:((float)g.getCurrentMilli()/ mTotalClockedTime) * 360f;

            canvas.drawArc(mCircle,startAngle,arc, true, mColorPaints.get(i));
            canvas.drawArc(mCircle,startAngle,arc, true, mLinePaint);
            startAngle+= arc;
        }
        if(mUseUnclockedTime){
            canvas.drawArc(mCircle, startAngle, 360 - startAngle, true, mUnclokedPaint);
            canvas.drawArc(mCircle, startAngle, 360 - startAngle, true, mLinePaint);
        }
    }


    private void prepColors(){
        for(int colorBin =1; colorBin < 8; colorBin++) {
            int red = colorBin / 4 > 0 ? 255 : 0;
            int blue = colorBin % 2 == 0 ? 0 : 255;
            int green = colorBin == 2 || colorBin == 3 || colorBin == 6 ? 255 : 0;
            Paint p = new Paint();
            p.setStyle(Paint.Style.FILL);
            p.setColor(Color.argb(255, red, green, blue));
            mColorPaints.add(p);
        }
    }

    public ArrayList<Paint> getColorPaints() {
        return mColorPaints;
    }

    public void setUseUnclockedTime(boolean useUnclockedTime) {
        if(mUseUnclockedTime!= useUnclockedTime){
            mUseUnclockedTime = useUnclockedTime;
            invalidate();
        }
    }

    public int getUnclockedTime(){
        return TOTAL_WEEK_TIME - mTotalClockedTime;
    }
}
