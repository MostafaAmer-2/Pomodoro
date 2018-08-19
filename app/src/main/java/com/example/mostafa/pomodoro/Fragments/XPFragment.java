package com.example.mostafa.pomodoro.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mostafa.pomodoro.R;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_xp, null);
        ButterKnife.bind(this, view);
        //TODO: Get xp from firebase and save in preferences
//        points = Preferences.loadPoints(getActivity().getApplicationContext());
        points = 150;
        calculateLevelAndPercentage();
        setUIViews();
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
        percentage = (level * 100) - points;

    }

}
