package com.example.mostafa.pomodoro.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.example.mostafa.pomodoro.Fragments.SettingsFragment;
import com.example.mostafa.pomodoro.Fragments.TimerFragment;
import com.example.mostafa.pomodoro.Fragments.XPFragment;
import com.example.mostafa.pomodoro.Fragments.trelloLogin;
import com.example.mostafa.pomodoro.R;
import com.example.mostafa.pomodoro.Settings.Preferences;
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
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.my_toolbar)
    Toolbar mTopToolbar;

    Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigator);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
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
                    case R.id.action_settings:
                        return loadFragment(new SettingsFragment());
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
            LoginManager.getInstance().logOut();
            Preferences.saveUserID(getApplicationContext(),"");
            realm.beginTransaction();
            realm.deleteAll();
            realm.commitTransaction();
            goToAuthentication();
        }


        return super.onOptionsItemSelected(item);
    }
//    private void checkFBLoginStatus() {
//        //Checking on fb login status
//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
//        Log.i("checkStatus", "onCreateView: FB status"+isLoggedIn+"  token:"+accessToken);
//    }

    private void goToAuthentication() {
        Intent go_to_Authentication = new Intent(getApplicationContext(), AuthenticationActivity.class);
        startActivity(go_to_Authentication);
    }

    public boolean loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        return true;
    }

}
