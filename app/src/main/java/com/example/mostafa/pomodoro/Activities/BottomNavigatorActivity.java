package com.example.mostafa.pomodoro.Activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.example.mostafa.pomodoro.Fragments.TimerFragment;
import com.example.mostafa.pomodoro.Fragments.trelloLogin;
import com.example.mostafa.pomodoro.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class BottomNavigatorActivity extends AppCompatActivity {

    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigator);
        ButterKnife.bind(this);
        handleButtonNavigationFunctionality();
        //TODO: remove realm initialization from here
        Realm.init(getApplicationContext());

    }

    private void handleButtonNavigationFunctionality() {
        loadFragment(new TimerFragment());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_main:
                        return loadFragment(new TimerFragment());
                    case R.id.action_boards:
                        return loadFragment(new trelloLogin());
                    default:
                        return false;
                }
            }
        });
    }

    public boolean loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        return true;
    }

}
