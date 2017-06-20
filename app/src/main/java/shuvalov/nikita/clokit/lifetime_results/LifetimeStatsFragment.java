package shuvalov.nikita.clokit.lifetime_results;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import shuvalov.nikita.clokit.GoalSQLHelper;
import shuvalov.nikita.clokit.R;
import shuvalov.nikita.clokit.graph_views.LineGraphView;
import shuvalov.nikita.clokit.pojos.Goal;


public class LifetimeStatsFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener{
    private String mGoalName;
    private ViewPager mViewPager;
    private ImageView mChartButton, mStatsButton;
    private Button mCancelButton, mRenameButton;
    private RelativeLayout mChartBg, mStatsBg;
    private LifetimeStatsPagerAdapter mLifetimeStatsPagerAdapter;
    private View mBottomPanel, mFauxNavBar;
    private boolean mOptionsDisplayed;



    public static final String GOAL_NAME = "Goal name";

    public LifetimeStatsFragment() {
        // Required empty public constructor
    }

    public static LifetimeStatsFragment newInstance(String goalName) {
        LifetimeStatsFragment fragment = new LifetimeStatsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(GOAL_NAME, goalName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOptionsDisplayed = false;
        if(getArguments()!=null){
            mGoalName = getArguments().getString(GOAL_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_lifetime_stats, container, false);

        ArrayList<Goal> goals = GoalSQLHelper.getInstance(getContext()).getLifetimeListForGoal(mGoalName);
        LifetimeStatsManager lifetimeStatsManager = LifetimeStatsManager.getInstance();
        lifetimeStatsManager.setGoalName(mGoalName);
        lifetimeStatsManager.setLifetimeGoalList(goals);
        mLifetimeStatsPagerAdapter = new LifetimeStatsPagerAdapter(getChildFragmentManager());

        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mViewPager.setCurrentItem(lifetimeStatsManager.getSelectedOption());

        mFauxNavBar = view.findViewById(R.id.faux_bot_navbar);
        mChartButton = (ImageView) view.findViewById(R.id.chart_option);
        mStatsButton = (ImageView) view.findViewById(R.id.stats_option);
        mChartBg = (RelativeLayout)view.findViewById(R.id.chart_bg);
        mStatsBg = (RelativeLayout)view.findViewById(R.id.stats_bg);
        mBottomPanel = view.findViewById(R.id.bottom_options_panel);
        mCancelButton = (Button)view.findViewById(R.id.cancel_action);
        mRenameButton = (Button)view.findViewById(R.id.merge_action);

        changeButtonColors();

        mChartButton.setOnClickListener(this);
        mStatsButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
        mRenameButton.setOnClickListener(this);

        mViewPager.setAdapter(mLifetimeStatsPagerAdapter);
        mViewPager.addOnPageChangeListener(this);


        return view;
    }

    private void changeButtonColors(){
        if(LifetimeStatsManager.getInstance().getSelectedOption() == 0){
            mChartButton.setImageResource(R.drawable.ic_show_chart_selected);
            mStatsButton.setImageResource(R.drawable.ic_list);
            mChartBg.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            mStatsBg.setBackgroundColor(Color.WHITE);
        }else{
            mChartButton.setImageResource(R.drawable.ic_show_chart);
            mStatsButton.setImageResource(R.drawable.ic_list_selected);
            mStatsBg.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            mChartBg.setBackgroundColor(Color.WHITE);
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chart_option:
                LifetimeStatsManager.getInstance().setSelectedOption(0);
                mViewPager.setCurrentItem(0);
                changeButtonColors();
                break;
            case R.id.stats_option:
                LifetimeStatsManager.getInstance().setSelectedOption(1);
                mViewPager.setCurrentItem(1);
                changeButtonColors();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        LifetimeStatsManager.getInstance().setSelectedOption(position);
        changeButtonColors();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void replaceBottomView(final View viewToHide, final View viewToShow) {
        Animation hideAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_panel_hide);
        hideAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewToHide.clearAnimation();
                showView(viewToShow);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        viewToHide.setAnimation(hideAnim);
        viewToHide.setVisibility(View.INVISIBLE);
    }

    public void showView(final View v) {
        Animation showAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_panel_show);
        showAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.setAnimation(showAnim);
        v.setVisibility(View.VISIBLE);
    }

    public void swapPanelsIfNeeded(boolean displayEditOptions){
        if(displayEditOptions && !mOptionsDisplayed) {
            replaceBottomView(mFauxNavBar, mBottomPanel);
            mOptionsDisplayed = true;
        }else if (!displayEditOptions && mOptionsDisplayed){
            replaceBottomView(mBottomPanel, mFauxNavBar);
            mOptionsDisplayed = false;
        }
    }

    public Button getCancelButton(){
        return mCancelButton;
    }

    public Button getRenameButton(){
        return mRenameButton;
    }
}
