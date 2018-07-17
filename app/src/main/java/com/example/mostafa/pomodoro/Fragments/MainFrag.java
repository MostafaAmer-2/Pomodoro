package com.example.mostafa.pomodoro.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mostafa.pomodoro.R;

public class MainFrag extends Fragment {

    private String key="51eb6eb13ad2f6cc5bcb87fc923ea427";
    private String secret="512eb8d81b5a5e139c64a58d49b471e6f3c7b572123423cc99705dc3323c76be";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Intent intent=new Intent(Intent.ACTION_VIEW, Uri
                .parse("https://trello.com/1/authorize?response_type=token&key=eb4ed77b19dc6f4e5be2602e3a02f4ff&return_url=https%3A%2F%2Fdevelopers.trello.com&callback_method=postMessage&scope=read%2Cwrite&expiration=1day&name=Trello+Sandbox"));
        startActivity(intent);

        return inflater.inflate(R.layout.fragment_main,null);
    }

    @Override
    public void onResume() {
        super.onResume();


    }
}
