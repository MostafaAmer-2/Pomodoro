package com.dreidev.mostafa.pomodoro.Presenter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.dreidev.mostafa.pomodoro.Fragments.TimerFragment;
import com.dreidev.mostafa.pomodoro.Model.TODOitem;
import com.dreidev.mostafa.pomodoro.Network.Network_timer;
import com.dreidev.mostafa.pomodoro.R;
import com.dreidev.mostafa.pomodoro.RecyclerViewAdapter_TODOs;
import com.dreidev.mostafa.pomodoro.RecyclerViewAdapter_TODOs_done;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class Presenter_TODOitems {
    //Lists of items
    private ArrayList<TODOitem> items = new ArrayList<>();
    private ArrayList<TODOitem> doneItems = new ArrayList<>();
    //Variables for handling item selection
    private TODOitem currentItem;
    private RecyclerViewAdapter_TODOs.ViewHolder currentHolder;
    //Recycler views
    private RecyclerViewAdapter_TODOs adapter;
    private RecyclerViewAdapter_TODOs_done doneAdapter;
    //Local variables
    private TimerFragment timerFragment;
    private Network_timer network;
    private Context ctx;
    Realm realm;

    public Presenter_TODOitems(TimerFragment TimerFragment, Context ctx) {
        this.timerFragment = TimerFragment;
        this.ctx = ctx;
        realm = Realm.getDefaultInstance();
        network = new Network_timer(this, ctx);
        setAdaptersAndUpdateLists();
    }

    //================================================================================
    // Init adapters
    //================================================================================

    private void setAdaptersAndUpdateLists() {
        updateBothItemsLists();
        setAdapterForTODOitems();
        setAdapterForDoneItems();
        getTimerFragment().getRecyclerView_todoList().setLayoutManager(new LinearLayoutManager(ctx));
    }

    private void setAdapterForDoneItems() {
        doneAdapter = new RecyclerViewAdapter_TODOs_done(this, doneItems);
        timerFragment.getRecyclerView_doneList().setAdapter(doneAdapter);
        timerFragment.getRecyclerView_doneList().setLayoutManager(new LinearLayoutManager(ctx));
        timerFragment.getRecyclerView_doneList().setNestedScrollingEnabled(false);
        checkIfDoneItemsIsEmpty();
    }

    public void updateBothItemsLists() {
        items.clear();
        doneItems.clear();
        RealmResults<TODOitem> cache = realm.where(TODOitem.class).equalTo("done", false).findAll();
        RealmResults<TODOitem> cacheDone = realm.where(TODOitem.class).equalTo("done", true).findAll();
        items.addAll(cache);
        doneItems.addAll(cacheDone);
    }

    private void setAdapterForTODOitems() {
        adapter = new RecyclerViewAdapter_TODOs(this, items);
        timerFragment.getRecyclerView_todoList().setAdapter(adapter);
        timerFragment.getRecyclerView_todoList().setLayoutManager(new LinearLayoutManager(ctx));
        timerFragment.getRecyclerView_todoList().setNestedScrollingEnabled(false);
        checkIfTodoItemsIsEmpty();
    }

    //================================================================================
    // Addition and removal of items
    //================================================================================

    public void addItem(TODOitem item) {
        items.add(item);
        notifyAdapter();
    }

    public void addDoneItem(TODOitem item) {
        doneItems.add(item);
        notifyDoneAdapter();
    }

    public void removeItem(TODOitem item) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getDescription().equals(item.getDescription()))
                items.remove(i);
        }
        notifyAdapter();
    }

    public void removeDoneItem(TODOitem item) {
        for (int i = 0; i < doneItems.size(); i++) {
            if (doneItems.get(i).getDescription().equals(item.getDescription()))
                doneItems.remove(i);
        }
        notifyDoneAdapter();
    }

    private void addItemToRecyclerView(TODOitem itemToBeAdded) {
        if (!itemToBeAdded.isDone()) {
            addItem(itemToBeAdded);
        } else {
            addDoneItem(itemToBeAdded);
        }
    }

    //================================================================================
    // Click listeners
    //================================================================================

    public void onItemClicked(RecyclerViewAdapter_TODOs.ViewHolder holder, TODOitem item) {
        if (currentHolder == null) {
            currentHolder = holder;
            currentItem = item;
            holder.getParent_layout().setBackgroundColor(ctx.getResources().getColor(R.color.pomodoroDarkBlue));
        } else if (currentHolder.equals(holder)) {
            currentHolder = null;
            currentItem = null;
            holder.getParent_layout().setBackgroundColor(ctx.getResources().getColor(R.color.pomodoroBlueTrans));
        } else {
            currentHolder.getParent_layout().setBackgroundColor(ctx.getResources().getColor(R.color.pomodoroBlueTrans));
            currentHolder = holder;
            currentItem = item;
            holder.getParent_layout().setBackgroundColor(ctx.getResources().getColor(R.color.pomodoroDarkBlue));
        }
    }

    public void onAddBtnClicked() {
        String itemEntered = timerFragment.getItemNameField().getText().toString();
        timerFragment.getItemNameField().setText("");
        realm.beginTransaction();
        TODOitem newItem = realm.createObject(TODOitem.class);
        newItem.setDescription(itemEntered);
        realm.commitTransaction();
        addItemToRecyclerView(newItem);
        //adding to firebase
        network.addItem(newItem);
    }

    //================================================================================
    // Adapting to items change
    //================================================================================

    public void notifyBothAdapters() {
        notifyAdapter();
        notifyDoneAdapter();
    }

    private void notifyAdapter() {
        checkIfTodoItemsIsEmpty();
        adapter.notifyDataSetChanged();
    }

    private void notifyDoneAdapter() {
        checkIfDoneItemsIsEmpty();
        doneAdapter.notifyDataSetChanged();
    }

    private void checkIfDoneItemsIsEmpty() {
        if (!doneItems.isEmpty()) {
            timerFragment.doneListNotEmpty();
        } else {
            timerFragment.doneListIsEmpty();
        }
    }

    private void checkIfTodoItemsIsEmpty() {
        if (!items.isEmpty()) {
            timerFragment.todoListNotEmpty();
        } else {
            timerFragment.todoListIsEmpty();
        }
    }

    //================================================================================
    // Getters and Setters
    //================================================================================

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

}
