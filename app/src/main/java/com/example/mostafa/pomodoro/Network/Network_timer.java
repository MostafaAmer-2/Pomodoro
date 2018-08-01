package com.example.mostafa.pomodoro.Network;

import android.content.Context;

import com.example.mostafa.pomodoro.Model.TODOitem;
import com.example.mostafa.pomodoro.Presenter.Presenter_timer;
//import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Network_timer {

    //Root node
    private DatabaseReference dref = FirebaseDatabase.getInstance().getReference("ha");
    //Child from the root node: users
    private DatabaseReference usersRef = dref.child("items");
    //Child from the users node: depending on the id of the user
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
        usersRef.addChildEventListener(new ChildEventListener() {
            /**
             * Add the new item present in the database to the ArrayList
             * @param dataSnapshot
             * @param s
             */
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TODOitem itemToBeAdded = TODOitem.convertToItem(dataSnapshot);
                presenter.addItem(itemToBeAdded);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addItem(TODOitem newItem) {
        usersRef.child(newItem.getDescription()).setValue("");
    }


}
