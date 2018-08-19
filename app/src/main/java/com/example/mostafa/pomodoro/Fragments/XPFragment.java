package com.example.mostafa.pomodoro.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mostafa.pomodoro.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XPFragment extends Fragment {

    private int xpPoints;

    @BindView(R.id.xp_count)
    TextView xp_count;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_xp, null);
        ButterKnife.bind(this, view);
        xpPoints = 100;
        xp_count.setText("Your total XP count is: " + xpPoints);
        return view;

    }
}
