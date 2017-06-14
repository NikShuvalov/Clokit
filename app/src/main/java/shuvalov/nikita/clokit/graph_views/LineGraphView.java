package shuvalov.nikita.clokit.graph_views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.View;

import java.util.ArrayList;

import shuvalov.nikita.clokit.AppUtils;
import shuvalov.nikita.clokit.pojos.Goal;

/**
 * Created by NikitaShuvalov on 6/14/17.
 */

public class LineGraphView extends View {
    private ArrayList<Goal> mLifetimeGoal;
    private Paint mGoalLinePaint, mProgressLinePaint, mGoalPaint, mProgressPaint;
    private Path mGoalPath, mProgressPath;
    private Rect mLineGraphRect;
    private float mVerticalScale;

    public LineGraphView(Context context, ArrayList<Goal> goals) {
        super(context);
        mLifetimeGoal = goals;
        mLineGraphRect= new Rect();
        preparePaints();
    }

    private void preparePaints(){
        mGoalLinePaint = new Paint();
        mGoalLinePaint.setStyle(Paint.Style.STROKE);
        mGoalLinePaint.setARGB(255, 0,0,0);
        mGoalLinePaint.setStrokeWidth(4f);
        mGoalLinePaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0f));

        mProgressLinePaint = new Paint();
        mProgressLinePaint.setStyle(Paint.Style.STROKE);
        mProgressLinePaint.setARGB(255, 0, 0 ,0);
        mProgressLinePaint.setStrokeWidth(4f);

        mGoalPaint = new Paint();
        mGoalPaint.setStyle(Paint.Style.FILL);
        mGoalPaint.setARGB(130, 255, 0 ,0);

        mProgressPaint = new Paint();
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setARGB(125, 0 , 255, 0);
    }

    private void definePaths(){
        int totalGoalTime = 0;
        int totalProgressTime = 0;
        if(mLifetimeGoal==null || mLifetimeGoal.isEmpty()) {
            return;
        }
        int startWeek = mLifetimeGoal.get(0).getWeekNum();
        int endWeek = AppUtils.getCurrentWeekNum();
        float interval = mLineGraphRect.width()/((endWeek - startWeek) +1f);

        mGoalPath = new Path();
        mProgressPath = new Path();
        mProgressPath.moveTo(mLineGraphRect.left, mLineGraphRect.bottom);
        mGoalPath.moveTo(mLineGraphRect.left, mLineGraphRect.bottom);

        for(int i = 0; i < mLifetimeGoal.size(); i ++){
            Goal goal = mLifetimeGoal.get(i);
            totalGoalTime+=goal.getEndMilli()/60000;
            totalProgressTime+= goal.getCurrentMilli()/60000;
            mProgressPath.lineTo(interval * i, mLineGraphRect.bottom - (totalProgressTime*mVerticalScale));
            mGoalPath.lineTo(interval*i, mLineGraphRect.bottom - (totalGoalTime * mVerticalScale));
        }
        mProgressPath.lineTo(mLineGraphRect.right,  mLineGraphRect.bottom - (totalProgressTime*mVerticalScale));
        mGoalPath.lineTo(mLineGraphRect.right,  mLineGraphRect.bottom - (totalGoalTime * mVerticalScale));
        mProgressPath.lineTo(mLineGraphRect.right, mLineGraphRect.bottom);
        mGoalPath.lineTo(mLineGraphRect.right, mLineGraphRect.bottom);
        mProgressPath.close();
        mGoalPath.close();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int width = right - left;
        int height = bottom - top;
        int centerY = (top+bottom)/2;
        if (height > width) {
            height = (int) (width / 1.78);
        }
        mLineGraphRect.set(left, centerY - height/2, right, centerY + height/2);
        determineVerticalScale(height *.9f);
        definePaths();
    }

    private void determineVerticalScale(float maxHeight){
        int totalProgress = 0;
        int totalGoal = 0;
        for(Goal g: mLifetimeGoal){
            totalProgress+= g.getCurrentMilli()/60000;
            totalGoal+= g.getEndMilli()/60000;
        }
        mVerticalScale = totalProgress > totalGoal ?
                maxHeight/totalProgress:
                maxHeight/totalGoal;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGraph(canvas);
    }

    private void drawGraph(Canvas canvas){
        if(mGoalPath != null && mProgressPath != null) {
            canvas.drawPath(mGoalPath, mGoalPaint);
            canvas.drawPath(mProgressPath, mProgressPaint);
            canvas.drawPath(mGoalPath, mGoalLinePaint);
            canvas.drawPath(mProgressPath, mProgressLinePaint);
        }
    }
}
