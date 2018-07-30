package com.example.mostafa.pomodoro.Presenter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.example.mostafa.pomodoro.Fragments.timer;
import com.example.mostafa.pomodoro.Model.TODOitem;
import com.example.mostafa.pomodoro.RecyclerViewAdapter_TODOs;


import java.util.ArrayList;

public class Presenter_timer {
    private ArrayList<TODOitem> items = new ArrayList<TODOitem>();
    private RecyclerViewAdapter_TODOs adapter;

    private timer timer;
//    private Network network;
    private Context ctx;

    public Presenter_timer(timer timer, Context ctx) {
        this.timer = timer;
        this.ctx = ctx;
//        network = new Network(this);

        adapter=new RecyclerViewAdapter_TODOs(this, items, ctx);
//        ((MainActivity) view).getRecyclerView().setAdapter(adapter);
//        ((MainActivity) view).getRecyclerView().setLayoutManager(new LinearLayoutManager(ctx));
    }

}
