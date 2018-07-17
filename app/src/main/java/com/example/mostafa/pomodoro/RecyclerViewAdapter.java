package com.example.mostafa.pomodoro;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mostafa.pomodoro.Model.TrelloBoard;
import com.example.mostafa.pomodoro.Presenter.Presenter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<TrelloBoard> mItems= new ArrayList<TrelloBoard>();
    private Context mContext;
    private Presenter presenter;

    public RecyclerViewAdapter(Presenter presenter, ArrayList<TrelloBoard> mItems, Context context){
        this.presenter=presenter;
        this.mItems=mItems;
        this.mContext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder= new ViewHolder(view);
        Log.i(TAG, "onCreateViewHolder: ana hnaaaa");
        return holder;
    }

    //TODO: stopped here (video called RecyclerView by CodingWithMitch at minute 13:45)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.i(TAG, "onBindViewHolder: item being added");
        if(!mItems.isEmpty() && holder != null && holder.itemName!= null) {
            Log.i(TAG, "onBindViewHolder: added");
            holder.itemName.setText(mItems.get(position).getName());
            holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                //    presenter.onItemClicked(position);
                    notifyDataSetChanged();
                    return false;
                }
            });
        }
        else{
            Log.i(TAG, "onBindViewHolder: failed");
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_name)
        TextView itemName;
        @BindView(R.id.parent_layout)
        RelativeLayout parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
