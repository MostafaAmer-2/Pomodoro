package com.example.mostafa.pomodoro.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mostafa.pomodoro.Activities.BottomNavigator;
import com.example.mostafa.pomodoro.R;
import com.example.mostafa.pomodoro.Settings.Preferences;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFrag extends Fragment {

    private static final String TAG = "MainFrag";
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.loginBtn)
    Button loginBtn;
    @BindView(R.id.enterButton)
    Button enterButton;
    @BindView(R.id.editText)
    EditText editText;

    private boolean returned=false;
    private String key="51eb6eb13ad2f6cc5bcb87fc923ea427";
    private String secret="512eb8d81b5a5e139c64a58d49b471e6f3c7b572123423cc99705dc3323c76be";

    public static String token="";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.fragment_main,null);;
        ButterKnife.bind(this, view);

        token=Preferences.loadData(getActivity().getApplicationContext());
        if(!token.equals("")){ //token already has a value stored
            Log.i(TAG, "onClick1: "+token);
            goToBoards();
        }
        else {

            Log.i(TAG, "onClick2: "+token);
            if (!returned) {
                linearLayout.setVisibility(View.INVISIBLE);
            }

            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    returned = true;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri
                            .parse("https://trello.com/1/authorize?expiration=never&scope=read,write,account&response_type=token&name=Server%20Token&key=51eb6eb13ad2f6cc5bcb87fc923ea427"));
                    startActivity(intent);
                    Toast.makeText(getActivity(), "Please Copy your token into your clipboard", Toast.LENGTH_LONG).show();
                }
            });

            enterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Preferences.saveData(getActivity().getApplicationContext(),editText.getText().toString());
                   // loadData();
                    goToBoards();
                }
            });

        }

        return view;
    }



    private void goToBoards() {
        ((BottomNavigator)getActivity()).loadFragment(new BoardsFrag(token));
        Log.i(TAG, "goToBoards: "+Preferences.loadData(getActivity().getApplicationContext()));
    }


    @Override
    public void onResume() {
        super.onResume();

        if(returned==true){
            linearLayout.setVisibility(View.VISIBLE);
        }
    }
}
