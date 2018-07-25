package com.example.mostafa.pomodoro.Presenter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.example.mostafa.pomodoro.Activities.BottomNavigator;
import com.example.mostafa.pomodoro.Fragments.trelloBoards;
import com.example.mostafa.pomodoro.Fragments.trelloLists;
import com.example.mostafa.pomodoro.Model.TrelloBoard;
import com.example.mostafa.pomodoro.Network.Network;
import com.example.mostafa.pomodoro.RecyclerViewAdapter_Boards;

import java.util.ArrayList;

public class Presenter_Boards {
    private ArrayList<TrelloBoard> items = new ArrayList<TrelloBoard>();
    private RecyclerViewAdapter_Boards adapter;

    private trelloBoards boardsFrag;
    private Network network;
    private Context ctx;


    public Presenter_Boards(trelloBoards view, Context ctx) {
        boardsFrag = view;
        this.ctx = ctx;

        network =new Network(this, ctx);
        initAdapter(ctx, items);

    }

    private void initAdapter(Context ctx, ArrayList<TrelloBoard> items) {
        adapter=new RecyclerViewAdapter_Boards(this, items, ctx);
        boardsFrag.getRecyclerView().setAdapter(adapter);
        boardsFrag.getRecyclerView().setLayoutManager(new LinearLayoutManager(ctx));
    }


    public Network getNetwork() {
        return network;
    }

    public ArrayList<TrelloBoard> getItems() {
        return items;
    }

    public void setItems(Context applicationContext, ArrayList<TrelloBoard> items) {
        this.items = items;
        initAdapter(applicationContext, items);
        //TODO: avoid initializing the adapter with an empty array
    }

    public trelloBoards getBoardsFrag() {
        return boardsFrag;
    }

    public void setBoardsFrag(trelloBoards boardsFrag) {
        this.boardsFrag = boardsFrag;
    }

    public void goToLists(){
        ((BottomNavigator)boardsFrag.getActivity()).loadFragment(new trelloLists());
    }
}


