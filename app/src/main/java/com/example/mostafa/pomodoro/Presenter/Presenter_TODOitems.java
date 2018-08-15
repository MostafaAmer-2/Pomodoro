package com.example.mostafa.pomodoro.Presenter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;

import com.example.mostafa.pomodoro.Fragments.TimerFragment;
import com.example.mostafa.pomodoro.Model.TODOitem;
import com.example.mostafa.pomodoro.Network.Network_timer;
import com.example.mostafa.pomodoro.RecyclerViewAdapter_TODOs;
import com.example.mostafa.pomodoro.RecyclerViewAdapter_TODOs_done;


import java.util.ArrayList;

public class Presenter_TODOitems {
    private ArrayList<TODOitem> items = new ArrayList<TODOitem>();
    private ArrayList<TODOitem> doneItems = new ArrayList<TODOitem>();

    private TODOitem currentItem;
    private RecyclerViewAdapter_TODOs.ViewHolder currentHolder;

    private RecyclerViewAdapter_TODOs adapter;
    private RecyclerViewAdapter_TODOs_done doneAdapter;

    private TimerFragment timerFragment;
    private Network_timer network;
    private Context ctx;

    public Presenter_TODOitems(TimerFragment TimerFragment, Context ctx) {
        this.timerFragment = TimerFragment;
        this.ctx=ctx;
        setAdapterForTODOitems();
        setAdapterForDoneItems();
        network = new Network_timer(this, ctx);
        }

    private void setAdapterForDoneItems() {
        doneAdapter=new RecyclerViewAdapter_TODOs_done(this, doneItems, ctx);
        timerFragment.getRecyclerView_doneList().setAdapter(doneAdapter);
        timerFragment.getRecyclerView_doneList().setLayoutManager(new LinearLayoutManager(ctx));
        timerFragment.getRecyclerView_doneList().setNestedScrollingEnabled(false);

    }

    private void setAdapterForTODOitems() {
        adapter=new RecyclerViewAdapter_TODOs(this, items);
        timerFragment.getRecyclerView_todoList().setAdapter(adapter);
        timerFragment.getRecyclerView_todoList().setLayoutManager(new LinearLayoutManager(ctx));
        timerFragment.getRecyclerView_todoList().setNestedScrollingEnabled(false);

    }

    public void onAddBtnClicked() {
        String itemEntered = timerFragment.getItemNameField().getText().toString();
        timerFragment.getItemNameField().setText("");
        TODOitem newItem = new TODOitem(itemEntered);
        network.addItemToRecyclerView(newItem);
        network.addItem(newItem);
    }

    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    public void addItem(TODOitem item) {
        items.add(item);
        notifyAdapter();
    }

    public void addDoneItem(TODOitem item) {
        doneItems.add(item);
        notifyDoneAdapter();
    }

    public void notifyDoneAdapter() {
        doneAdapter.notifyDataSetChanged();
    }

    public void removeItem(TODOitem item) {
        for (int i=0;i<items.size();i++) {
            if(items.get(i).getDescription().equals(item.getDescription()))
                items.remove(i);
        }
        notifyAdapter();
    }

    public ArrayList<TODOitem> getItems() {
        return items;
    }

    public TimerFragment getTimerFragment() {
        return timerFragment;
    }

    public Network_timer getNetwork() {
        return network;
    }

    public TODOitem getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(TODOitem currentItem) {
        this.currentItem = currentItem;
    }

    public RecyclerViewAdapter_TODOs.ViewHolder getCurrentHolder() {
        return currentHolder;
    }

    public void setCurrentHolder(RecyclerViewAdapter_TODOs.ViewHolder currentHolder) {
        this.currentHolder = currentHolder;
    }

    public ArrayList<TODOitem> getDoneItems() {
        return doneItems;
    }

    public void onItemClicked(RecyclerViewAdapter_TODOs.ViewHolder holder, TODOitem item) {
        int normal = Color.argb(255,226,193,199);
        int shaded = Color.argb(255,255,50,50);
        if(currentHolder==null){
            currentHolder=holder;
            currentItem=item;
            holder.getParent_layout().setBackgroundColor(shaded);
        }
        else if(currentHolder.equals(holder)){
            currentHolder=null;
            currentItem=null;
            holder.getParent_layout().setBackgroundColor(normal);
        }
        else{
            currentHolder.getParent_layout().setBackgroundColor(normal);
            currentHolder=holder;
            currentItem=item;
            holder.getParent_layout().setBackgroundColor(shaded);
        }
    }

    public void setItems(Context ctx, ArrayList<TODOitem> items) {
        this.items = items;
    }

    public void setDoneItems(ArrayList<TODOitem> doneItems) {
        this.doneItems = doneItems;
    }
}
