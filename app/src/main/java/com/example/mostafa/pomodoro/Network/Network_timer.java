package com.example.mostafa.pomodoro.Network;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.mostafa.pomodoro.Model.TODOitem;
import com.example.mostafa.pomodoro.Model.TrelloBoard;
import com.example.mostafa.pomodoro.Presenter.Presenter_TODOitems;
//import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class Network_timer {

    private DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference itemsRef = dref.child("items");
    private DatabaseReference doneRef = dref.child("done");

    private DatabaseReference usersRef = dref.child("users");
    private DatabaseReference IDref;// = usersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    Presenter_TODOitems presenter;
    Realm realm;


    public Network_timer(Presenter_TODOitems presenter, Context ctx) {
        this.presenter = presenter;
        addChildEventListener();

        // Initialize Realm (just once per application)
        io.realm.Realm.init(ctx);

// Get a Realm instance for this thread
        realm = io.realm.Realm.getDefaultInstance();



        RealmResults<TODOitem> cache = realm.where(TODOitem.class).equalTo("done", false).findAll();
        RealmResults<TODOitem> cacheDone = realm.where(TODOitem.class).equalTo("done", true).findAll();
        Log.i("Network_timer", "loadBoards0: "+cache.size());
        for(int i=0; i< cache.size();i++) {
            TODOitem cachedItem = cache.get(i);
            presenter.addItem(cachedItem);
        }
        for(int i=0; i< cacheDone.size();i++){
            TODOitem cachedItem = cacheDone.get(i);
            presenter.addDoneItem(cachedItem);
        }
        presenter.notifyAdapter();
        presenter.notifyDoneAdapter();
        presenter.getTimerFragment().getRecyclerView_todoList().setLayoutManager(new LinearLayoutManager(ctx));

    }

    public void addItemToRecyclerView(TODOitem itemToBeAdded){
        if (!itemToBeAdded.isDone()) {
            presenter.addItem(itemToBeAdded);
            presenter.notifyAdapter();
        } else {
            presenter.addDoneItem(itemToBeAdded);
            presenter.notifyDoneAdapter();

        }
        moveToRealm(itemToBeAdded);
    }
    private void addChildEventListener() {
        itemsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                boolean present=false;
//                RealmResults<TODOitem> realmResults=realm.where(TODOitem.class).findAll();
//                for(int i=0; i<realmResults.size();i++){
//                    if(realmResults.get(i).getDescription().equals(itemToBeAdded.getDescription())){
//                        present=true;
//                        break;
//                    }
//                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                presenter.notifyAdapter();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeFromRealm(TODOitem.convertToItem(dataSnapshot));
//                presenter.removeItem(TODOitem.convertToItem(dataSnapshot));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                presenter.notifyAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                presenter.notifyAdapter();
            }
        });
    }

    private void removeFromRealm(TODOitem itemToBeRemoved) {
        realm.beginTransaction();
        TODOitem realmItem = realm.where(TODOitem.class).equalTo("description", itemToBeRemoved.getDescription()).findFirst();
//        Log.i("Network_timer", "removeFromRealm: "+realmItem.isValid());
//        realmItem.deleteFromRealm();
        Log.i("Network_timer", "loadBoards2: " + realm.where(TODOitem.class).findAll().size());
        realm.commitTransaction();ArrayList<TODOitem> tmp=new ArrayList<TODOitem>();
//        ArrayList<TODOitem> tmpDone=new ArrayList<TODOitem>();
//        RealmResults<TODOitem> realmResults=realm.where(TODOitem.class).equalTo("done",false).findAll();
//        for(int i=0; i<realmResults.size();i++){
//            if (!realmResults.get(i).isDone()) {
//                tmp.add(realmResults.get(i));
//            } else {
//                tmpDone.add(realmResults.get(i));
//
//            }
//        }
//        presenter.setItems(presenter.getTimerFragment().getActivity().getApplicationContext(),tmp);
//        presenter.setDoneItems(tmpDone);
    }

    private void moveToRealm(TODOitem itemToBeAdded) {
        realm.beginTransaction();
        realm.copyToRealm(itemToBeAdded);
        realm.commitTransaction();
    }

    public void addItem(TODOitem newItem) {
        itemsRef.child(newItem.getDescription()).child("isDone").setValue(newItem.isDone());
        itemsRef.child(newItem.getDescription()).child("pomodoros").setValue(newItem.getPomodoros());
    }


    public void updatePomodoros(TODOitem itemSelected) {
        itemsRef.child(itemSelected.getDescription()).child("pomodoros").setValue(itemSelected.getPomodoros());
    }

    public DatabaseReference getItemsRef() {
        return itemsRef;
    }

    public void removeNode(String name) {
        presenter.getNetwork().getItemsRef().child(name).setValue(null);
        presenter.notifyAdapter();

    }

    public void addNode(String itemName, boolean done, int pomodoros) {
        presenter.getNetwork().getItemsRef().child(itemName).child("isDone").setValue(done);
        presenter.getNetwork().getItemsRef().child(itemName).child("pomodoros").setValue(pomodoros);

    }

    public void onCloseUpdateCache(){
        realm.beginTransaction();
        realm.deleteAll();
        realm.insert(presenter.getItems());
        realm.insertOrUpdate(presenter.getDoneItems());
        realm.commitTransaction();
    }

    //TODO: Not needed for now
//    public void markDone(TODOitem itemSelected) {
//        itemsRef.child(itemSelected.getDescription()).child("isDone").setValue(itemSelected.isDone());
//    }
}
