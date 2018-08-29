package com.example.mostafa.pomodoro.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.example.mostafa.pomodoro.Network.Network_Boards;
import com.example.mostafa.pomodoro.R;
import com.example.mostafa.pomodoro.Settings.Preferences;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.json.JSONArray;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class SplashScreenActivity extends AppCompatActivity{
    private static final String TAG = "SplashScreenActivity";
    @BindView(R.id.imageView)
    ImageView splashScreenImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Hide ActionBar for this activity
        getSupportActionBar().hide();

        doSplashScreenAnimation();
        checkTokenValidityAndUpdatePrefrences();
        // Initialize Realm (just once per application)
        Realm.init(getApplicationContext());
    }

    private void goToAuthentication() {
        Intent go_to_Authentication = new Intent(getApplicationContext(), AuthenticationActivity.class);
        startActivity(go_to_Authentication);
    }
    private void goToBottomNavigatorActivity() {
        Intent go_to_BottomNavigation = new Intent(getApplicationContext(), BottomNavigatorActivity.class);
        startActivity(go_to_BottomNavigation);
    }

    private void doSplashScreenAnimation() {
        final Animation clockwiseAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clockwise);
        splashScreenImage.startAnimation(clockwiseAnimation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                splashScreenImage.startAnimation(clockwiseAnimation);
            }
        }, 10000);
    }

    public void checkTokenValidityAndUpdatePrefrences() {
        String token = Preferences.loadTrelloToken(getApplicationContext());
        Network_Boards network = new Network_Boards(null, getApplicationContext());

        if (Preferences.isTokenPresent(getApplicationContext())) {
            //initialized once to access methods of Network class. Will be initialized again in trelloBoards
            network.testTokenValid(token).done(new DoneCallback<JSONArray>() {
                @Override
                public void onDone(JSONArray result) {
                    Preferences.saveDataFlag(getApplicationContext(), "true");
                    Log.i(TAG, "onDone: 1"+Preferences.loadUserID(getApplicationContext()));
                    if(!Preferences.loadUserID(getApplicationContext()).equals("")){
                        goToBottomNavigatorActivity();
                    }
                    else{
                        goToAuthentication();
                    }
                }
            }).fail(new FailCallback<VolleyError>() {
                @Override
                public void onFail(VolleyError result) {
                    Preferences.saveDataFlag(getApplicationContext(), "false");
                    Log.i(TAG, "onDone: 2"+Preferences.loadUserID(getApplicationContext()));
                    if(!Preferences.loadUserID(getApplicationContext()).equals("")){
                        goToBottomNavigatorActivity();
                    }
                    else{
                        goToAuthentication();
                    }
                }
            });
        } else {
            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Preferences.saveDataFlag(getApplicationContext(), "false");
                    Log.i(TAG, "onDone: 3"+Preferences.loadUserID(getApplicationContext()));
                    if(!Preferences.loadUserID(getApplicationContext()).equals("")){
                        goToBottomNavigatorActivity();
                    }
                    else{
                        goToAuthentication();
                    }
                }
            }, 2000);
        }
    }
}
