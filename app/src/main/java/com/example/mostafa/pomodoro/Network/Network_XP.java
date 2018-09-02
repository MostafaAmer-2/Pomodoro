package com.example.mostafa.pomodoro.Network;

import android.content.Context;
import android.support.annotation.NonNull;
import com.example.mostafa.pomodoro.Fragments.XPFragment;
import com.example.mostafa.pomodoro.Settings.Preferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.realm.Realm;

public class Network_XP {


    private DatabaseReference dref;
    private DatabaseReference xpRef;

    private Realm realm;
    private Context ctx;
    private XPFragment frag;


    public Network_XP(XPFragment frag, Context ctx) {
        this.ctx=ctx;
        this.frag=frag;

        dref = FirebaseDatabase.getInstance().getReference().child(Preferences.loadUserID(ctx));
        DatabaseReference itemsRef = dref.child("items");
        xpRef = dref.child("xp");

        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild("xp")) {
                    dref.child("xp").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
