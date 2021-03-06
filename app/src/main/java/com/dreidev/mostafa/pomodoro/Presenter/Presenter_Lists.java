package com.dreidev.mostafa.pomodoro.Presenter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.dreidev.mostafa.pomodoro.Activities.BottomNavigatorActivity;
import com.dreidev.mostafa.pomodoro.Fragments.trelloCards;
import com.dreidev.mostafa.pomodoro.Fragments.trelloLists;
import com.dreidev.mostafa.pomodoro.Model.TrelloList;
import com.dreidev.mostafa.pomodoro.Network.Network_Lists;
import com.dreidev.mostafa.pomodoro.RecyclerViewAdapter_Lists;

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
        listsFrag.getRecyclerView_lists().setAdapter(adapter);
        listsFrag.getRecyclerView_lists().setLayoutManager(new LinearLayoutManager(ctx));
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
    }

    public void goToCards(String token, String listID){
        ((BottomNavigatorActivity)listsFrag.getActivity()).loadFragment(new trelloCards(token,listID),true);
    }
}
