package shuvalov.nikita.clokit.LifetimeResults;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import shuvalov.nikita.clokit.POJOs.Goal;
import shuvalov.nikita.clokit.R;

/**
 * Created by NikitaShuvalov on 3/6/17.
 */

public class LifetimeRecyclerAdapter extends RecyclerView.Adapter<LifetimeViewHolder> {
    private ArrayList<Goal> mLifetimeResults;

    public LifetimeRecyclerAdapter(ArrayList<Goal> lifetimeResults) {
        mLifetimeResults = lifetimeResults;
    }

    @Override
    public LifetimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LifetimeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_lifetime,null));
    }

    @Override
    public void onBindViewHolder(LifetimeViewHolder holder, int position) {
        holder.bindDataToViews(mLifetimeResults.get(position));
    }

    @Override
    public int getItemCount() {
        return mLifetimeResults.size();
    }
}
