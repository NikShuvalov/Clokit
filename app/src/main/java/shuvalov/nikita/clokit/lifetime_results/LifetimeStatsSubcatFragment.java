package shuvalov.nikita.clokit.lifetime_results;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import shuvalov.nikita.clokit.AppConstants;
import shuvalov.nikita.clokit.AppUtils;
import shuvalov.nikita.clokit.GoalSQLHelper;
import shuvalov.nikita.clokit.R;
import shuvalov.nikita.clokit.pojos.Goal;


public class LifetimeStatsSubcatFragment extends Fragment implements LifeTimeBreakdownRecyclerAdapter.GoalCheckedListener, View.OnClickListener{
    private RecyclerView mRecyclerView;
    private LifeTimeBreakdownRecyclerAdapter mAdapter;
    private Button mCancelButton, mMergeButton;

    public LifetimeStatsSubcatFragment() {
    }

    public static LifetimeStatsSubcatFragment newInstance() {
        return new LifetimeStatsSubcatFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lifetime_stats_subcat, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.subcat_recycler);
        mCancelButton = ((LifetimeStatsFragment)getParentFragment()).getCancelButton();
        mMergeButton = ((LifetimeStatsFragment)getParentFragment()).getRenameButton();
        mCancelButton.setOnClickListener(this);
        mMergeButton.setOnClickListener(this);

        ArrayList<Goal> goalList = LifetimeStatsManager.getInstance().getLifetimeGoalList();
        Collections.sort(goalList,new TimeAllocatedComparator());
        setUpRecycler(goalList);
        return view;
    }

    private void setUpRecycler(ArrayList< Goal > goals){
        mAdapter = new LifeTimeBreakdownRecyclerAdapter(goals, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onCheckedStatusChange(boolean b) {
        ((LifetimeStatsFragment)getParentFragment()).swapPanelsIfNeeded(b);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.cancel_action:
                mAdapter.unselectAll();
                break;
            case R.id.merge_action:
                createDialog();

                break;
        }
    }

    private void createDialog(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_rename, null);
        final EditText nameEntry = (EditText)view.findViewById(R.id.name_entry);
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("Rename", null).create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialogInterface) {
                Button confirmButton = ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_POSITIVE);
                final EditText editText = (EditText)((AlertDialog) dialogInterface).findViewById(R.id.name_entry);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String entered = editText.getText().toString();
                        if(!entered.trim().equals("")){
                            renameSelectedGoals(entered);
                            dialogInterface.dismiss();
                        }else{
                            nameEntry.setError("Can't be empty");
                        }
                    }
                });

            }
        });
        alertDialog.show();
    }

    private void renameSelectedGoals(String newSubCat){
        ArrayList<String> selectedGoals = mAdapter.getSelectedSubCategories();
        for(String subcat: selectedGoals){
            if(AppUtils.isGoalCurrentlyActive(getContext().getSharedPreferences(AppConstants.PREFERENCES_NAME, Context.MODE_PRIVATE),LifetimeStatsManager.getInstance().getGoalName(),subcat)) {
                String messageText = String.format("Active Category: \"%s\" wasn't edited",subcat);
                Toast.makeText(getContext(), messageText, Toast.LENGTH_LONG).show();
            }else{
                GoalSQLHelper.getInstance(getContext()).refactorGoalSubCat(LifetimeStatsManager.getInstance().getGoalName(), subcat, newSubCat);

            }
        }
        mAdapter.unselectAll();
        updatelifetimeStatsList();
    }

    private void updatelifetimeStatsList(){
        LifetimeStatsManager lifetimeStatsManager = LifetimeStatsManager.getInstance();
        ArrayList<Goal> goals = GoalSQLHelper.getInstance(getContext()).getLifetimeListForGoal(lifetimeStatsManager.getGoalName());
        Collections.sort(goals,new TimeAllocatedComparator());
        lifetimeStatsManager.setLifetimeGoalList(goals);
        mAdapter.updateGoalList(lifetimeStatsManager.getLifetimeGoalList());
    }
}
