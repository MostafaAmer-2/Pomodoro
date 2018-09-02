package com.dreidev.mostafa.pomodoro.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dreidev.mostafa.pomodoro.Activities.BottomNavigatorActivity;
import com.dreidev.mostafa.pomodoro.R;
import com.dreidev.mostafa.pomodoro.Settings.Preferences;

import butterknife.BindView;
import butterknife.ButterKnife;

public class trelloLogin extends Fragment {

    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.loginBtn)
    Button loginBtn;
    @BindView(R.id.enterButton)
    Button enterTokenButton;
    @BindView(R.id.editText)
    EditText tokenField;

    private boolean returnedFromLogin = false;
    public static String token = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_trello_login, null);
        ;
        ButterKnife.bind(this, view);

        //Trying to set the selected item from the bottom navigator from here
        if(((BottomNavigatorActivity)getActivity()).bottomNavigationView.getSelectedItemId()!=R.id.action_boards){
            ((BottomNavigatorActivity)getActivity()).bottomNavigationView.setSelectedItemId(R.id.action_boards);
            //pop one fragment from the back stack because it gets duplicated
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.popBackStack();
        }

        loadToken();
        if (Preferences.isTokenPresent(getActivity().getApplicationContext()) && Preferences.loadDataFlag(getActivity().getApplicationContext()).equals("true")) { //token already has a value stored
            goToBoards();
        } else { //No Valid token present
            if (!returnedFromLogin) { //Still did not go to trello login page
                linearLayout.setVisibility(View.INVISIBLE);
            }
            setButtonListeners();
        }

        return view;
    }

    private void setButtonListeners() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnedFromLogin = true;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri
                        .parse("https://trello.com/1/authorize?expiration=never&scope=read,write,account&response_type=token&name=Server%20Token&key=51eb6eb13ad2f6cc5bcb87fc923ea427"));
                startActivity(intent);
                Toast.makeText(getActivity(), "Please Copy your token into your clipboard", Toast.LENGTH_LONG).show();
            }
        });

        enterTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Preferences.saveTrelloToken(getActivity().getApplicationContext(), tokenField.getText().toString());
                goToBoards();
            }
        });
    }

    private void loadToken() {
        token = Preferences.loadTrelloToken(getActivity().getApplicationContext());
    }


    private void goToBoards() {
        ((BottomNavigatorActivity) getActivity()).loadFragment(new trelloBoards(token),false);
    }


    @Override
    public void onResume() {
        super.onResume();

        if (returnedFromLogin) {
            linearLayout.setVisibility(View.VISIBLE);
        }
    }
}
