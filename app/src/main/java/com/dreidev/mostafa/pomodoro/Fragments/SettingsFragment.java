package com.dreidev.mostafa.pomodoro.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dreidev.mostafa.pomodoro.Activities.AuthenticationActivity;
import com.dreidev.mostafa.pomodoro.Activities.BottomNavigatorActivity;
import com.dreidev.mostafa.pomodoro.R;
import com.dreidev.mostafa.pomodoro.Settings.Preferences;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SettingsFragment extends Fragment {
    @BindView(R.id.logout)
    TextView logout;
    @BindView(R.id.contactUs)
    TextView contactUsButton;
    @BindView(R.id.backButton)
    Button backButton;
    @BindView(R.id.settingsLayout)
    View settingsLayout;
    @BindView(R.id.contactUsLayout)
    View contactUsLayout;

    Realm realm;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);

        //Trying to set the selected item from the bottom navigator from here
        if(((BottomNavigatorActivity)getActivity()).bottomNavigationView.getSelectedItemId()!=R.id.action_settings){
            ((BottomNavigatorActivity)getActivity()).bottomNavigationView.setSelectedItemId(R.id.action_settings);
            //pop one fragment from the back stack because it gets duplicated
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.popBackStack();
        }

        realm = Realm.getDefaultInstance();
        settingsLayout.setVisibility(View.VISIBLE);
        contactUsLayout.setVisibility(View.GONE);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Preferences.saveUserID(getActivity().getApplicationContext(), "");
                realm.beginTransaction();
                realm.deleteAll();
                realm.commitTransaction();
                Preferences.saveTrelloToken(getActivity().getApplicationContext(),"");
                goToAuthentication();

                getActivity().finish();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsLayout.setVisibility(View.VISIBLE);
                contactUsLayout.setVisibility(View.GONE);
            }
        });
        contactUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsLayout.setVisibility(View.GONE);
                contactUsLayout.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    private void goToAuthentication() {
        Intent go_to_Authentication = new Intent(getApplicationContext(), AuthenticationActivity.class);
        startActivity(go_to_Authentication);
    }
}
