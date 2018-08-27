package com.example.mostafa.pomodoro.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.example.mostafa.pomodoro.Fragments.TimerFragment;
import com.example.mostafa.pomodoro.Fragments.XPFragment;
import com.example.mostafa.pomodoro.Fragments.trelloLogin;
import com.example.mostafa.pomodoro.R;
import com.example.mostafa.pomodoro.Settings.Preferences;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BottomNavigatorActivity extends AppCompatActivity {

    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.my_toolbar)
    Toolbar mTopToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigator);
        ButterKnife.bind(this);
        setSupportActionBar(mTopToolbar);
        handleButtonNavigationFunctionality();
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
                    case R.id.action_XP:
                        return loadFragment(new XPFragment());
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Preferences.saveUserID(getApplicationContext(),"");
            goToAuthentication();
        }


        return super.onOptionsItemSelected(item);
    }

    private void goToAuthentication() {
        Intent go_to_Authentication = new Intent(getApplicationContext(), AuthenticationActivity.class);
        startActivity(go_to_Authentication);
    }

    public boolean loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        return true;
    }

}
