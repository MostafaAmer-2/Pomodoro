package com.dreidev.mostafa.pomodoro.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dreidev.mostafa.pomodoro.Activities.BottomNavigatorActivity;
import com.dreidev.mostafa.pomodoro.Network.Network_XP;
import com.dreidev.mostafa.pomodoro.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XPFragment extends Fragment {

    private int points;
    private int level;
    private int percentage;

    @BindView(R.id.xp_points)
    TextView text_xpPoints;
    @BindView(R.id.level)
    TextView text_level;
    @BindView(R.id.percentage)
    TextView text_percentage;
    @BindView(R.id.XPProgressBar)
    ProgressBar xp_progressBar;
    @BindView(R.id.loading)
    ProgressBar loading;

    Network_XP network;

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_xp, null);
        ButterKnife.bind(this, view);

        //Trying to set the selected item from the bottom navigator from here
        if(((BottomNavigatorActivity)getActivity()).bottomNavigationView.getSelectedItemId()!=R.id.action_XP){
            ((BottomNavigatorActivity)getActivity()).bottomNavigationView.setSelectedItemId(R.id.action_XP);
            //pop one fragment from the back stack because it gets duplicated
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.popBackStack();
        }

        network=new Network_XP(this, getActivity().getApplicationContext());
        network.getXP();
        return view;
    }

    private void setUIViews() {
        text_xpPoints.setText("XP points: " + points);
        text_level.setText("Level " + level);
        text_percentage.setText(percentage + "%");
        xp_progressBar.setProgress(percentage);
    }

    private void calculateLevelAndPercentage() {
        level = (points / 100) + 1;
        percentage = 100-((level * 100) - points);

    }

    public void continueOps(int xp) {
        points = xp;
        hideProgressBar();
        calculateLevelAndPercentage();
        setUIViews();
    }

    //only hideProgressBar is needed as the progress bar is visible on the startup of the fragment
    public void hideProgressBar(){
        loading.setVisibility(View.INVISIBLE);
        text_percentage.setVisibility(View.VISIBLE);
        text_xpPoints.setVisibility(View.VISIBLE);
        text_level.setVisibility(View.VISIBLE);
    }
}
