package com.example.mostafa.pomodoro.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mostafa.pomodoro.Activities.BottomNavigator;
import com.example.mostafa.pomodoro.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFrag extends Fragment {

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

    public static final String SHARED_PREFS="sharedPrefs";
    public static String token=null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.fragment_main,null);;
        ButterKnife.bind(this, view);

        loadData();
        if(token!git=null){
            goToBoards();
        }
        else {

            if (returned == false) {
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
                    saveData();
                   // loadData();
                    goToBoards();
                }
            });

        }

        return view;
    }

    private void loadData() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        token = sharedPref.getString("token", null);
    }

    private void saveData() {
        SharedPreferences sharedPreferences=getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", editText.getText().toString());
        token=editText.getText().toString();
        editor.commit();
    }


    private void goToBoards() {
        ((BottomNavigator)getActivity()).loadFragment(new BoardsFrag(token));
    }


    @Override
    public void onResume() {
        super.onResume();

        if(returned==true){
            linearLayout.setVisibility(View.VISIBLE);
        }
    }
}
