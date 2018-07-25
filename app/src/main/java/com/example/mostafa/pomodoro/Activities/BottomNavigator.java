package com.example.mostafa.pomodoro.Activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.mostafa.pomodoro.Fragments.trelloLogin;
import com.example.mostafa.pomodoro.R;

public class BottomNavigator extends AppCompatActivity {

  //  Presenter_Boards presenter; //Can be found in the fragment itself (the actual view)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigator);

        BottomNavigationView bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragmentToBeLoaded=null;
                switch(item.getItemId()){
                    case R.id.action_main:
                        //   fragmentToBeLoaded = new trelloBoards();
                        break;

                    case R.id.action_boards:
                        fragmentToBeLoaded = new trelloLogin();
                        break;
                }
                return loadFragment(fragmentToBeLoaded);
            }
        });

        loadFragment(new trelloLogin());
    }

    public boolean loadFragment(Fragment fragment){

        if(fragment!=null){
            getSupportFragmentManager().
                    beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null)
            Log.i("SASAA", "onResume: "+bundle.toString());
        else
            Log.i("SASAA", "onResume: nsaing");
    }
}
