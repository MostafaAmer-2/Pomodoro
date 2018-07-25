package com.example.mostafa.pomodoro.Presenter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.example.mostafa.pomodoro.Fragments.trelloLists;
import com.example.mostafa.pomodoro.Model.TrelloList;
import com.example.mostafa.pomodoro.Network.Network_Boards;
import com.example.mostafa.pomodoro.Network.Network_Lists;
import com.example.mostafa.pomodoro.RecyclerViewAdapter_Lists;

import java.util.ArrayList;

public class Presenter_Lists {
    private ArrayList<TrelloList> items = new ArrayList<TrelloList>();
    private RecyclerViewAdapter_Lists adapter;

    private trelloLists listsFrag;
    private Network_Lists network;
    private Context ctx;

    public Presenter_Lists(trelloLists view, Context ctx) {
        listsFrag = view;
        this.ctx = ctx;


        network =new Network_Lists(this, ctx);
        initAdapter(ctx, items);

    }

    private void initAdapter(Context ctx, ArrayList<TrelloList> items) {
        adapter=new RecyclerViewAdapter_Lists(this, items, ctx);
        listsFrag.getRecyclerView().setAdapter(adapter);
        listsFrag.getRecyclerView().setLayoutManager(new LinearLayoutManager(ctx));
    }


    public Network_Lists getNetwork() {
        return network;
    }

    public ArrayList<TrelloList> getItems() {
        return items;
    }

    public void setItems(Context applicationContext, ArrayList<TrelloList> items) {
        this.items = items;
        initAdapter(applicationContext, items);
        //TODO: avoid initializing the adapter with an empty array
    }

    public trelloLists getListsFrag() {
        return listsFrag;
    }

    public void setListsFrag(trelloLists listsFrag) {
        this.listsFrag = listsFrag;
    }
}
