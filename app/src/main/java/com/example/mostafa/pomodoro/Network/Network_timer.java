package com.example.mostafa.pomodoro.Network;

import android.content.Context;

import com.example.mostafa.pomodoro.Model.TODOitem;
import com.example.mostafa.pomodoro.Presenter.Presenter_TODOitems;
import com.example.mostafa.pomodoro.Settings.Preferences;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.realm.Realm;
import io.realm.RealmResults;

//import com.google.firebase.auth.FirebaseAuth;

public class Network_timer {

    private DatabaseReference dref;
    private DatabaseReference itemsRef;

    Presenter_TODOitems presenter;
    Realm realm;
    Context ctx;


    public Network_timer(Presenter_TODOitems presenter, Context ctx) {
        this.presenter = presenter;
        this.ctx = ctx;

        dref = FirebaseDatabase.getInstance().getReference().child(Preferences.loadUserID(ctx));
        itemsRef = dref.child("items");

        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild("xp")) {
                    dref.child("xp").setValue(0); //To create an XP node for the item if still doesn't have one
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addChildEventListener();
        // Get a Realm instance for this thread
        realm = io.realm.Realm.getDefaultInstance();
    }


    private void addChildEventListener() {
        itemsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TODOitem newItem = TODOitem.convertToItem(dataSnapshot);
                RealmResults<TODOitem> realmResults = realm.where(TODOitem.class).equalTo("description", newItem.getDescription()).findAll();
                if (realmResults.size() == 0) {
                    realm.beginTransaction();
                    realm.copyToRealm(newItem);
                    realm.commitTransaction();
                    presenter.updateBothItemsLists();
                    presenter.notifyBothAdapters();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                presenter.notifyBothAdapters();
            }
        });
    }

    public void removeFromRealm(TODOitem itemToBeRemoved) {
        realm.beginTransaction();
        itemToBeRemoved.deleteFromRealm();
        realm.commitTransaction();
    }

    public void addItem(TODOitem newItem) {
        String nodeName = newItem.getDescription();
        nodeName = nodeName.replace(".", "");
        nodeName = nodeName.replace("#", "");
        nodeName = nodeName.replace("$", "");
        nodeName = nodeName.replace("[", "");
        nodeName = nodeName.replace("]", "");
        itemsRef.child(nodeName).child("isDone").setValue(newItem.isDone());
        itemsRef.child(nodeName).child("pomodoros").setValue(newItem.getPomodoros());
    }


    public void updatePomodoros(TODOitem itemSelected) {
        itemsRef.child(itemSelected.getDescription()).child("pomodoros").setValue(itemSelected.getPomodoros());
    }

    public DatabaseReference getItemsRef() {
        return itemsRef;
    }

    public void removeNode(String name) {
        presenter.getNetwork().getItemsRef().child(name).setValue(null);
        presenter.notifyBothAdapters();

    }

    public void markNodeDone(String name) {
        presenter.getNetwork().getItemsRef().child(name).child("isDone").setValue(true);
        presenter.notifyBothAdapters();

    }

    public void addNode(String itemName, boolean done, int pomodoros) {
        presenter.getNetwork().getItemsRef().child(itemName).child("isDone").setValue(done);
        presenter.getNetwork().getItemsRef().child(itemName).child("pomodoros").setValue(pomodoros);

    }

    public void onCloseUpdateCache() {
        realm.beginTransaction();
        realm.deleteAll();
        realm.insert(presenter.getItems());
        realm.insertOrUpdate(presenter.getDoneItems());
        realm.commitTransaction();
    }

}
