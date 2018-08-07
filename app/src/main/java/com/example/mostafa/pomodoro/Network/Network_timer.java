package com.example.mostafa.pomodoro.Network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.mostafa.pomodoro.Model.TODOitem;
import com.example.mostafa.pomodoro.Presenter.Presenter_timer;
//import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Network_timer {

    private DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference itemsRef = dref.child("items");
    private DatabaseReference doneRef = dref.child("done");

    private DatabaseReference usersRef = dref.child("users");
    private DatabaseReference IDref;// = usersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    Presenter_timer presenter;


    public Network_timer(Presenter_timer presenter, Context ctx) {
        this.presenter = presenter;
        addChildEventListener();
    }

    /**
     * Method to add the child event listener to the reference
     */
    private void addChildEventListener() {
        itemsRef.addChildEventListener(new ChildEventListener() {
            /**
             * Add the new item present in the database to the ArrayList
             * @param dataSnapshot
             * @param s
             */
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

//                for(DataSnapshot data:dataSnapshot.getChildren()){
//                    Log.i("Testing FB", data.toString());
//                    Log.i("Testing FB Key", data.getKey());
//                    Log.i("Testing FB Value", data.getValue().toString());
//
//                }
//                Log.i("Testing FB Value", dataSnapshot.getValue().toString());

                TODOitem itemToBeAdded = TODOitem.convertToItem(dataSnapshot);
               if(!itemToBeAdded.isDone())
                   presenter.addItem(itemToBeAdded);
               else
                   presenter.addItemDone(itemToBeAdded);
//                Log.i("Network_timer", "Item"+itemToBeAdded.getDescription());
//                Log.i("pst", "onChildAdded: "+dataSnapshot.child("pomodoros").getValue().toString());
//                itemToBeAdded.setPomodoros((int)(long)dataSnapshot.child("pomodoros").getValue());
//                itemToBeAdded.setDone((boolean) dataSnapshot.child("isDone").getValue());
//                Log.i("pst2", "onChildAdded: "+dataSnapshot.child("isDone").getValue());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                presenter.notifyAdapter();
            }

            /**
             * Remove the item that was deleted from the database, from the ArrayList
             * @param dataSnapshot
             */
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
