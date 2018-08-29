package com.example.mostafa.pomodoro.Navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.example.mostafa.pomodoro.R;

public class Navigation {

    public static void doActivityNavigation(Context current, Activity next, Boolean pushed){
        Intent go_to_Authentication = new Intent(current,next.getClass());
        current.startActivity(go_to_Authentication);
        if(!pushed)
            ((Activity) current).finish();
    }

    public static void doFragmentNavigation(Context current, Fragment next, Boolean pushed){
        if(!pushed)
            ((AppCompatActivity)current).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, next).commit();
        else
            ((AppCompatActivity)current).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, next).addToBackStack(null).commit();

    }
}
