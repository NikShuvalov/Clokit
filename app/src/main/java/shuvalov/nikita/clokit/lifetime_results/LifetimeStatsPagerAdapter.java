package shuvalov.nikita.clokit.lifetime_results;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by NikitaShuvalov on 6/16/17.
 */

public class LifetimeStatsPagerAdapter extends FragmentPagerAdapter {

    public LifetimeStatsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return LifetimeStatsChartFragment.newInstance();
            case 1:
                return LifetimeStatsSubcatFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }


}
