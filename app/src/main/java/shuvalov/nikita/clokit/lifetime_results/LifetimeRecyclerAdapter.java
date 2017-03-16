package shuvalov.nikita.clokit.lifetime_results;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import shuvalov.nikita.clokit.pojos.Goal;
import shuvalov.nikita.clokit.R;

/**
 * Created by NikitaShuvalov on 3/6/17.
 */

public class LifetimeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Goal> mLifetimeResults;
    public static final int HEADER = 0;
    public static final int CHILD = 1;

    public LifetimeRecyclerAdapter(ArrayList<Goal> lifetimeResults) {
        mLifetimeResults = lifetimeResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==HEADER){
            return new LifetimeHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_lifetime_header, null));
        }
        return new LifetimeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_lifetime,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()==0){
            ((LifetimeHeaderViewHolder) holder).bindDataToViews(mLifetimeResults.get(position));
        }else{
            ((LifetimeViewHolder)holder).bindDataToViews(mLifetimeResults.get(position));

        }
    }

    @Override
    public int getItemCount() {
        return mLifetimeResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return HEADER;
        }
        return CHILD;
    }
}
