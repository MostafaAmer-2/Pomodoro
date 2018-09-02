package com.example.mostafa.pomodoro.Activities;

import android.content.Context;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import com.example.mostafa.pomodoro.Fragments.PageAdapter;
import com.example.mostafa.pomodoro.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class AuthenticationActivity extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem tabChats;
    TabItem tabStatus;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frags);
        tabLayout = findViewById(R.id.tablayout);
        tabChats = findViewById(R.id.tabLogin);
        tabStatus = findViewById(R.id.tabRegister);
        viewPager = findViewById(R.id.viewPager);
        //Hide ActionBar for this activity
        getSupportActionBar().hide();


        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void finishActivity() {
        finish();
    }
}
