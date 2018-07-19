package com.example.mostafa.pomodoro.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.example.mostafa.pomodoro.Model.TrelloBoard;
import com.example.mostafa.pomodoro.Network.Network;
import com.example.mostafa.pomodoro.R;
import com.example.mostafa.pomodoro.Settings.Preferences;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.json.JSONArray;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    RequestQueue requestQueue;
    ArrayList<TrelloBoard> boards1 = new ArrayList<>();

    private Network network;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView image = (ImageView)findViewById(R.id.imageView);
        final Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.clockwise);
        image.startAnimation(animation1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                image.startAnimation(animation1);
            }
        }, 10000);

        network =new Network(null, getApplicationContext());
        token= Preferences.loadData(getApplicationContext());

        Log.i(TAG, "testing token: "+ Preferences.loadData(getApplicationContext()));
        if(Preferences.isTokenPresent(getApplicationContext())){
            isTokenValid(Preferences.loadData(getApplicationContext()));
            Log.i("SASA", "inside");
        }
        else{
            Log.i("SASA", "outside");
            Preferences.saveDataFlag(getApplicationContext(),false);
            Intent go_to_BottomNavigator = new Intent(getApplicationContext(), BottomNavigator.class);
            startActivity(go_to_BottomNavigator);
        }



    }




    public void isTokenValid(String token){
        network.testTokenValid(token).done(new DoneCallback<JSONArray>() {
            @Override
            public void onDone(JSONArray result) {
                Preferences.saveDataFlag(getApplicationContext(),true);

                Intent go_to_BottomNavigator = new Intent(getApplicationContext(), BottomNavigator.class);
                startActivity(go_to_BottomNavigator);
            }
        });

        network.testTokenValid(token).fail(new FailCallback<VolleyError>() {
            @Override
            public void onFail(VolleyError result) {
                Intent go_to_BottomNavigator = new Intent(getApplicationContext(), BottomNavigator.class);
                startActivity(go_to_BottomNavigator);
            }
        });
    }


    public void clockwise(View view){
        ImageView image = (ImageView)findViewById(R.id.imageView);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.clockwise);
        image.startAnimation(animation1);

    }

    public void zoom(View view){

        ImageView image = (ImageView)findViewById(R.id.imageView);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.myanimation);
        image.startAnimation(animation);
    }

    public void fade(View view){
        ImageView image = (ImageView)findViewById(R.id.imageView);
        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.fade);
        image.startAnimation(animation1);
    }

    public void blink(View view){
        ImageView image = (ImageView)findViewById(R.id.imageView);
        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
        image.startAnimation(animation1);
    }

    public void move(View view){
        ImageView image = (ImageView)findViewById(R.id.imageView);
        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
        image.startAnimation(animation1);
    }

    public void slide(View view){
        ImageView image = (ImageView)findViewById(R.id.imageView);
        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide);
        image.startAnimation(animation1);
    }

}
