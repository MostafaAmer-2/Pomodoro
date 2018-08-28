package com.example.mostafa.pomodoro.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mostafa.pomodoro.Activities.AuthenticationActivity;
import com.example.mostafa.pomodoro.R;
import com.example.mostafa.pomodoro.Settings.Preferences;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SettingsFragment extends Fragment {
    @BindView(R.id.logout)
    TextView logout;
    @BindView(R.id.contactUs)
    TextView contactUs;

    Realm realm;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        realm = Realm.getDefaultInstance();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Preferences.saveUserID(getActivity().getApplicationContext(),"");
                realm.beginTransaction();
                realm.deleteAll();
                realm.commitTransaction();
                goToAuthentication();
            }
        });

        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }

    private void goToAuthentication() {
        Intent go_to_Authentication = new Intent(getApplicationContext(), AuthenticationActivity.class);
        startActivity(go_to_Authentication);
    }
}
