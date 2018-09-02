package com.dreidev.mostafa.pomodoro.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.dreidev.mostafa.pomodoro.Fragments.SettingsFragment;
import com.dreidev.mostafa.pomodoro.Fragments.TimerFragment;
import com.dreidev.mostafa.pomodoro.Fragments.XPFragment;
import com.dreidev.mostafa.pomodoro.Fragments.trelloLogin;
import com.dreidev.mostafa.pomodoro.R;
import com.dreidev.mostafa.pomodoro.Settings.Preferences;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class BottomNavigatorActivity extends AppCompatActivity {

    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.bottom_navigation)
    public BottomNavigationView bottomNavigationView;


    Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigator);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        handleButtonNavigationFunctionality();

        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    private void handleButtonNavigationFunctionality() {
        loadFragment(new TimerFragment(),true);
        bottomNavigationView.setOnNavigationItemSelectedListener(listener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0) {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);

        }

    }

    BottomNavigationView.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_main:
                    return loadFragment(new TimerFragment(),true);
                case R.id.action_boards:
                    return loadFragment(new trelloLogin(),true);
                case R.id.action_XP:
                    return loadFragment(new XPFragment(),true);
                case R.id.action_settings:
                    return loadFragment(new SettingsFragment(),true);
                default:
                    return false;
            }
        }
    };

    public boolean loadFragment(Fragment fragment, Boolean pushed) {
        if(pushed)
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
        else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
//            getSupportFragmentManager().popBackStack();

        }

        return true;
    }

}
