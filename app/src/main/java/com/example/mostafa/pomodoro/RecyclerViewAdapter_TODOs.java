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

import com.example.mostafa.pomodoro.Model.TODOitem;
import com.example.mostafa.pomodoro.Presenter.Presenter_timer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter_TODOs extends RecyclerView.Adapter<RecyclerViewAdapter_TODOs.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter_TODOs";
    private ArrayList<TODOitem> mItems= new ArrayList<TODOitem>();
    private Context mContext;
    private Presenter_timer presenter;

    public RecyclerViewAdapter_TODOs(Presenter_timer presenter, ArrayList<TODOitem> mItems, Context context){
        this.presenter=presenter;
        this.mItems=mItems;
        this.mContext=context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder= new ViewHolder(view);
        return holder;
    }

    //TODO: stopped here (video called RecyclerView by CodingWithMitch at minute 13:45)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if(!mItems.isEmpty() && holder != null && holder.itemName!= null) {
            holder.itemName.setText(mItems.get(position).getDescription());
        }
        else{
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
