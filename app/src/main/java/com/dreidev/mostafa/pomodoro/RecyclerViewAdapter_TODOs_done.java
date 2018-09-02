package com.dreidev.mostafa.pomodoro;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dreidev.mostafa.pomodoro.Model.TODOitem;
import com.dreidev.mostafa.pomodoro.Presenter.Presenter_TODOitems;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class RecyclerViewAdapter_TODOs_done extends RecyclerView.Adapter<RecyclerViewAdapter_TODOs_done.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter_TODOs";
    private ArrayList<TODOitem> mItems;
    private Presenter_TODOitems presenter;
    private Realm realm;

    public RecyclerViewAdapter_TODOs_done(Presenter_TODOitems presenter, ArrayList<TODOitem> mItems) {
        this.presenter = presenter;
        this.mItems = mItems;
        realm = Realm.getDefaultInstance();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_done, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (!mItems.isEmpty() && holder != null && holder.itemName != null) {
            holder.itemName.setText(mItems.get(position).getDescription());
            presenter.getNetwork().getItemsRef().child(mItems.get(position).getDescription()).child("pomodoros");
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_name)
        TextView itemName;
        @BindView(R.id.parent_layout)
        LinearLayout parentLayout;
        @BindView(R.id.deleteBtn)
        Button deleteBtn;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TODOitem itemSelected = presenter.getDoneItems().get(getAdapterPosition());
                    presenter.getNetwork().removeNode(itemSelected.getDescription());
                    presenter.removeDoneItem(itemSelected);
                    presenter.getNetwork().removeFromRealm(itemSelected);
                }
            });
        }
    }
}
