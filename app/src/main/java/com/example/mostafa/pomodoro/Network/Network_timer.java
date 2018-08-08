package com.example.mostafa.pomodoro.Network;

import android.content.Context;

import com.example.mostafa.pomodoro.Model.TODOitem;
import com.example.mostafa.pomodoro.Presenter.Presenter_TODOitems;
//import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Network_timer {

    private DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference itemsRef = dref.child("items");
    private DatabaseReference doneRef = dref.child("done");

    private DatabaseReference usersRef = dref.child("users");
    private DatabaseReference IDref;// = usersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    Presenter_TODOitems presenter;


    public Network_timer(Presenter_TODOitems presenter, Context ctx) {
        this.presenter = presenter;
        addChildEventListener();
    }

    private void addChildEventListener() {
        itemsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TODOitem itemToBeAdded = TODOitem.convertToItem(dataSnapshot);
               if(!itemToBeAdded.isDone())
                   presenter.addItem(itemToBeAdded);
               else
                   presenter.addDoneItem(itemToBeAdded);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                presenter.notifyAdapter();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                presenter.removeItem(TODOitem.convertToItem(dataSnapshot));
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

    //TODO: Not needed for now
//    public void markDone(TODOitem itemSelected) {
//        itemsRef.child(itemSelected.getDescription()).child("isDone").setValue(itemSelected.isDone());
//    }
}
