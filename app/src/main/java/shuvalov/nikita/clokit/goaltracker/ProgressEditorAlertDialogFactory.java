package shuvalov.nikita.clokit.goaltracker;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import shuvalov.nikita.clokit.AppUtils;
import shuvalov.nikita.clokit.R;
import shuvalov.nikita.clokit.pojos.Goal;
import shuvalov.nikita.clokit.pojos.Week;

/**
 * Created by NikitaShuvalov on 3/22/17.
 */

public class ProgressEditorAlertDialogFactory {
    private EditText mHourEntry, mMinuteEntry;
    private TextView mGoalNameText, mSubCatText, mEditedTimeText;
    private AlertDialog mAlertDialog;
    private Goal mCachedGoal;
    private ImageView mPlusTime, mMinusTime;
    private Button mZeroTimeButt;
    private long mEditedTime;
    private OnChangeConfirmListener mOnChangeConfirmListener;

    public ProgressEditorAlertDialogFactory(@NonNull Context context, @NonNull Goal goal, OnChangeConfirmListener onChangeConfirmListener) {
        mCachedGoal = goal;
        mOnChangeConfirmListener = onChangeConfirmListener;
        setUp(context);
    }

    public Goal getCachedGoal() {
        return mCachedGoal;
    }

    public long getEditedTime() {
        return mEditedTime;
    }

    private void setUp(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_editgoal, null);
        findViews(view);
        mAlertDialog = createAlertDialog(context, view);
        mAlertDialog.setOnShowListener(createOnShowListener());
        updateDisplayText(mCachedGoal);
    }

    private void findViews(View view){
        mHourEntry = (EditText) view.findViewById(R.id.hour_entry);
        mMinuteEntry = (EditText) view.findViewById(R.id.minute_entry);
        mGoalNameText = (TextView) view.findViewById(R.id.goalname_text);
        mSubCatText = (TextView) view.findViewById(R.id.subcat_text);
        mEditedTimeText = (TextView) view.findViewById(R.id.adjustment_time_text);
        mPlusTime = (ImageView) view.findViewById(R.id.add_time_butt);
        mMinusTime = (ImageView) view.findViewById(R.id.remove_time_butt);
        mZeroTimeButt = (Button) view.findViewById(R.id.set_zer0_butt);
    }

    private AlertDialog createAlertDialog(Context context, View view){
        return new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton("Save Changes", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
    }

    public void updateDisplayText(Goal goal){
        mGoalNameText.setText(goal.getGoalName());
        String subCatString = goal.getSubCategory();
        if(subCatString==null || subCatString.isEmpty()){
            mSubCatText.setText("-");
        }else{
            mSubCatText.setText(subCatString);
        }
        mEditedTimeText.setText(AppUtils.getHoursAndMinutes(Math.abs(mEditedTime)));

        adjustTimeColor(mEditedTime);
    }

    private void adjustTimeColor(long editedTime){
        if(999>= editedTime && editedTime>=-999 ){
            mEditedTimeText.setTextColor(Color.BLACK);
        }else if (editedTime>999){
            mEditedTimeText.setTextColor(Color.GREEN);
        }else{
            mEditedTimeText.setTextColor(Color.RED);
        }
    }

    public AlertDialog getAlertDialog() {
        return mAlertDialog;
    }

    private DialogInterface.OnShowListener createOnShowListener() {
        return new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button saveChangesButt = ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_POSITIVE);

                setZeroClickerLogic(mZeroTimeButt);
                setAddClickerLogic(mPlusTime);
                setRemoveClickerLogic(mMinusTime);
                //ToDo: When positive button is clicked we retrieve the changed goal and update the references in the adapter, the manager, and the sql references.
                setSaveClickerLogic(saveChangesButt, dialogInterface);
            }

            void setSaveClickerLogic(final Button saveChangesButt, final DialogInterface dialogInterface){
                saveChangesButt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mEditedTime!=0) {
                            //ToDo: Decide if I want lifeTimeTracking to be effected by users adding values. If So, add logic here to add that time.
                            mOnChangeConfirmListener.changeConfirmed();
                            dialogInterface.dismiss();
                        } else {
                            Toast.makeText(view.getContext(), "No changes were made", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            void setRemoveClickerLogic(ImageView minusTimeButton) {
                minusTimeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String hours = mHourEntry.getText().toString();
                        String minutes = mMinuteEntry.getText().toString();
                        if (hours.isEmpty() && minutes.isEmpty()) {
                            mHourEntry.setError("Need a value to add time");
                            mMinuteEntry.setError("Need a value to add time");
                        } else {
                            long timeRemoved = getTotalTime(hours, minutes);
                            if (timeRemoved == 0) {
                                Toast.makeText(view.getContext(), "To leave goal unchanged press Cancel instead", Toast.LENGTH_LONG).show();
                            } else {
                                long overKill = mCachedGoal.removeTimeSpent(timeRemoved);
                                mEditedTime -= timeRemoved;
                                if (overKill > 0) { //If the timeRemoved is greater than the amount of time there, the overkill number will be negative and will adjust for the extra value.
                                    mEditedTime += overKill;
                                }
                                updateDisplayText(mCachedGoal);
                            }
                        }
                    }

                });
            }

            void setAddClickerLogic(ImageView plusTimeButton) {
                plusTimeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String hours = mHourEntry.getText().toString();
                        String minutes = mMinuteEntry.getText().toString();
                        if (hours.isEmpty() && minutes.isEmpty()) {
                            mHourEntry.setError("Need a value to add time");
                            mMinuteEntry.setError("Need a value to add time");
                        } else {
                            long totalTimeAdd = getTotalTime(hours, minutes);
                            if (totalTimeAdd == 0) {
                                Toast.makeText(view.getContext(), "To leave goal unchanged press \"Cancel\" instead", Toast.LENGTH_LONG).show();
                            } else {
                                mCachedGoal.addTimeSpent(totalTimeAdd);
                                mEditedTime += totalTimeAdd;
                                updateDisplayText(mCachedGoal);
                            }
                        }
                    }
                });
            }

            void setZeroClickerLogic(Button zeroTimeButt) {
                zeroTimeButt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mEditedTime -= mCachedGoal.getCurrentMilli();
                        mCachedGoal.setCurrentMilli(0);
                        updateDisplayText(mCachedGoal);
                    }
                });
            }

            long getTotalTime(String hours, String minutes) {
                long hourValue = 0, minuteValue = 0;
                if (!hours.isEmpty()) {
                    hourValue = Long.valueOf(hours) * 60000 * 60;
                }
                if (!minutes.isEmpty()) {
                    minuteValue = Long.valueOf(minutes) * 60000;
                }
                return hourValue + minuteValue;
            }

        };
    }

    public void resetCachedData(){
        mEditedTime = 0;
        mCachedGoal = null;
    }

    public interface OnChangeConfirmListener{
        void changeConfirmed();
    }
}
