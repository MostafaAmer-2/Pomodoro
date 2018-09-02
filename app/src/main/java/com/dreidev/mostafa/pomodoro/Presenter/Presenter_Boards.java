package com.dreidev.mostafa.pomodoro.Presenter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.dreidev.mostafa.pomodoro.Activities.BottomNavigatorActivity;
import com.dreidev.mostafa.pomodoro.Fragments.trelloBoards;
import com.dreidev.mostafa.pomodoro.Fragments.trelloLists;
import com.dreidev.mostafa.pomodoro.Model.TrelloBoard;
import com.dreidev.mostafa.pomodoro.Network.Network_Boards;
import com.dreidev.mostafa.pomodoro.RecyclerViewAdapter_Boards;

import java.util.ArrayList;

public class Presenter_Boards {
    private ArrayList<TrelloBoard> items = new ArrayList<TrelloBoard>();
    private ArrayList<TrelloBoard> cache = new ArrayList<TrelloBoard>();
    private RecyclerViewAdapter_Boards adapter;

    private trelloBoards boardsFrag;
    private Network_Boards network;
    private Context ctx;


    public Presenter_Boards(trelloBoards view, Context ctx) {
        boardsFrag = view;
        this.ctx = ctx;

        network =new Network_Boards(this, ctx);
        initAdapter(ctx, items);

    }

    private void initAdapter(Context ctx, ArrayList<TrelloBoard> items) {
        adapter=new RecyclerViewAdapter_Boards(this, items, ctx);
        boardsFrag.getRecyclerView_boards().setAdapter(adapter);
        boardsFrag.getRecyclerView_boards().setLayoutManager(new LinearLayoutManager(ctx));
    }


    public Network_Boards getNetwork() {
        return network;
    }

    public ArrayList<TrelloBoard> getItems() {
        return items;
    }

    public ArrayList<TrelloBoard> getCache() {
        return cache;
    }

    public void setItems(Context applicationContext, ArrayList<TrelloBoard> items) {
        this.items = items;
        initAdapter(applicationContext, items);
    }

    public trelloBoards getBoardsFrag() {
        return boardsFrag;
    }

    public void goToLists(String token, String boardsID){
        trelloLists trelloLists= new trelloLists(token,boardsID);
        ((BottomNavigatorActivity)boardsFrag.getActivity()).loadFragment(trelloLists,true);
    }
}


