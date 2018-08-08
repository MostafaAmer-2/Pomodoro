package com.example.mostafa.pomodoro;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mostafa.pomodoro.Model.TrelloCard;
import com.example.mostafa.pomodoro.Presenter.Presenter_Cards;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter_Cards extends RecyclerView.Adapter<RecyclerViewAdapter_Cards.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter_Car";
    private ArrayList<TrelloCard> mItems= new ArrayList<TrelloCard>();
    private Presenter_Cards presenter;

    public RecyclerViewAdapter_Cards(Presenter_Cards presenter, ArrayList<TrelloCard> mItems){
        this.presenter=presenter;
        this.mItems=mItems;
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
        holder.cardName.setText(mItems.get(position).getName());
        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                presenter.onItemLongClicked(mItems.get(position).getName());
//                notifyDataSetChanged(); //generates new random coloured cards
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_name)
        TextView cardName;
        @BindView(R.id.parent_layout)
        CardView card;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            //create random colours for cards
            createRandomBackgroundColour();
        }

        private void createRandomBackgroundColour() {
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            cardName.setBackgroundColor(color);
        }
    }

}
