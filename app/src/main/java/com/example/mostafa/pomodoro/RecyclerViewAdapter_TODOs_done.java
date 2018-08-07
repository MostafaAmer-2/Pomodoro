package com.example.mostafa.pomodoro;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mostafa.pomodoro.Model.TODOitem;
import com.example.mostafa.pomodoro.Presenter.Presenter_TODOitems;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter_TODOs_done extends RecyclerView.Adapter<RecyclerViewAdapter_TODOs_done.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter_TODOs";
    private ArrayList<TODOitem> mItems= new ArrayList<TODOitem>();
    private Context mContext;
    private Presenter_TODOitems presenter;

    public RecyclerViewAdapter_TODOs_done(Presenter_TODOitems presenter, ArrayList<TODOitem> mItems, Context context){
        this.presenter=presenter;
        this.mItems=mItems;
        this.mContext=context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_done, parent, false);
        ViewHolder holder= new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if(!mItems.isEmpty() && holder != null && holder.itemName!= null) {
            holder.itemName.setText(mItems.get(position).getDescription());
            presenter.getNetwork().getItemsRef().child(mItems.get(position).getDescription()).child("pomodoros");
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
        ConstraintLayout parentLayout;
        @BindView(R.id.deleteBtn)
        Button deleteBtn;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TODOitem itemSelected= presenter.getDoneItems().get(getAdapterPosition());
                    presenter.getNetwork().removeNode(itemSelected.getDescription());
                    presenter.getDoneItems().remove(itemSelected);
                    presenter.notifyAdapterDone();
                }
            });



        }



    }
}
