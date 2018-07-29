package com.example.mostafa.pomodoro.Navigation;

import android.support.v4.app.Fragment;

public class Navigation {
    Fragment current;
    Fragment next;
    Boolean pushed;

    public Navigation(Fragment current, Fragment next, Boolean pushed){
        this.current=current;
        this.next=next;
        this.pushed=pushed;
    }
    public void doNavigation(Fragment current, Fragment next, Boolean pushed){

    }
}
