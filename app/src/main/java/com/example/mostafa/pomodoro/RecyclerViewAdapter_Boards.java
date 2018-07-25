package com.example.mostafa.pomodoro;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mostafa.pomodoro.Model.TrelloBoard;
import com.example.mostafa.pomodoro.Presenter.Presenter_Boards;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter_Boards extends RecyclerView.Adapter<RecyclerViewAdapter_Boards.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter_Boards";
    private ArrayList<TrelloBoard> mItems= new ArrayList<TrelloBoard>();
    private Context mContext;
    private Presenter_Boards presenter;

    public RecyclerViewAdapter_Boards(Presenter_Boards presenter, ArrayList<TrelloBoard> mItems, Context context){
        this.presenter=presenter;
        this.mItems=mItems;
        this.mContext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_carditem, parent, false);
        ViewHolder holder= new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if(!mItems.isEmpty() && holder != null && holder.itemName!= null) {
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
            //print outside
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
        CardView parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            //create random colours for cards
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            itemName.setBackgroundColor(color);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //I'v been clicked (Boards card)
                    presenter.goToLists();
                }
            });
        }
    }
}
