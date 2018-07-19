package com.example.mostafa.pomodoro.Presenter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.example.mostafa.pomodoro.Fragments.BoardsFrag;
import com.example.mostafa.pomodoro.Model.TrelloBoard;
import com.example.mostafa.pomodoro.Network.Network;
import com.example.mostafa.pomodoro.RecyclerViewAdapter;

import java.util.ArrayList;

public class Presenter {
    private ArrayList<TrelloBoard> items = new ArrayList<TrelloBoard>();
    private RecyclerViewAdapter adapter;

    private BoardsFrag boardsFrag;
    private Network network;
    private Context ctx;


    public Presenter(BoardsFrag view, Context ctx) {
        boardsFrag = view;
        this.ctx = ctx;

        network =new Network(this, ctx);
        initAdapter(ctx, items);

    }

    private void initAdapter(Context ctx, ArrayList<TrelloBoard> items) {
        adapter=new RecyclerViewAdapter(this, items, ctx);
        boardsFrag.getRecyclerView().setAdapter(adapter);
        boardsFrag.getRecyclerView().setLayoutManager(new LinearLayoutManager(ctx));
    }


    public Network getNetworkPresenter() {
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

    public BoardsFrag getBoardsFrag() {
        return boardsFrag;
    }

    public void setBoardsFrag(BoardsFrag boardsFrag) {
        this.boardsFrag = boardsFrag;
    }
}

//TODO: refactor the code
