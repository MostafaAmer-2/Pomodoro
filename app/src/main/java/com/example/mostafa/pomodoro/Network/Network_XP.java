package com.example.mostafa.pomodoro.Network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.mostafa.pomodoro.Fragments.XPFragment;
import com.example.mostafa.pomodoro.Presenter.Presenter_TODOitems;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.realm.Realm;

public class Network_XP {


    private DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference itemsRef = dref.child("items");
    private DatabaseReference xpRef = dref.child("xp");

    Realm realm;
    Context ctx;
    XPFragment frag;


    public Network_XP(XPFragment frag, Context ctx) {
        this.ctx=ctx;
        this.frag=frag;
        // Get a Realm instance for this thread
        realm = io.realm.Realm.getDefaultInstance();

    }

    public void getXP(){
        xpRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long xpValue = dataSnapshot.getValue(Long.class);
                frag.continueOps((int)(xpValue));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
