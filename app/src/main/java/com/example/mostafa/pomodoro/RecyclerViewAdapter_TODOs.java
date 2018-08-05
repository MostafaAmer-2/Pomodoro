package com.example.mostafa.pomodoro;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mostafa.pomodoro.Model.TODOitem;
import com.example.mostafa.pomodoro.Presenter.Presenter_timer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.mostafa.pomodoro.Model.TODOitem.decreasePomododro;
import static com.example.mostafa.pomodoro.Model.TODOitem.increasePomododro;

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
            presenter.getTimer().updateBtnText(holder.addPomodoroBtn, mItems.get(position));
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
        @BindView(R.id.addPomodoro)
        Button addPomodoroBtn;
        @BindView(R.id.removePomodoro)
        Button removePomodoroBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            addPomodoroBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TODOitem itemSelected= presenter.getItems().get(getAdapterPosition());
                    increasePomododro(itemSelected);
                    presenter.getTimer().updateBtnText(addPomodoroBtn, itemSelected);
                    presenter.getNetwork().updatePomodoros(itemSelected);
                }
            });
            removePomodoroBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TODOitem itemSelected= presenter.getItems().get(getAdapterPosition());
                    decreasePomododro(itemSelected);
                    presenter.getTimer().updateBtnText(addPomodoroBtn, itemSelected);
                    presenter.getNetwork().updatePomodoros(itemSelected);
                }
            });


        }



    }
}
