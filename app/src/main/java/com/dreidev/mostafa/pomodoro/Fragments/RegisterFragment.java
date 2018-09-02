package com.dreidev.mostafa.pomodoro.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dreidev.mostafa.pomodoro.Activities.AuthenticationActivity;
import com.dreidev.mostafa.pomodoro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {
    String TAG = "RegisterFragment";
    @BindView(R.id.emailField)
    EditText mEmailField;
    @BindView(R.id.passwordField)
     EditText mPasswordField;
    @BindView(R.id.registerBtn)
     Button mRegisterBtn;

    private FirebaseAuth mAuth;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);
        mRegisterBtn.setOnClickListener(new View.OnClickListener(){

            /**
             *starting the registration process once the "register" button gets clicked
             * @param view
             */
            @Override
            public void onClick(View view) {
                startRegistration();
            }
        });
        return view;
    }


    @Override
    public void onViewCreated( View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void startRegistration(){
        Log.i(TAG, "startRegistration: "+"HEREEEEE!!");
        final String email= mEmailField.getText().toString();
        final String password=mPasswordField.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) { //checking that the fields are not left empty
            Toast.makeText(getActivity(), "Empty Fields", Toast.LENGTH_LONG).show();
        }
        else if (!email.contains("@")){ //checking that the email is inserted in correct format
            Toast.makeText(getActivity(), "Incorrect e-mail format", Toast.LENGTH_LONG).show();
        }
        else if (password.length()<6) { //checking that password meets the required length
            Toast.makeText(getActivity(), "Password must contain 6 characters or more", Toast.LENGTH_LONG).show();
        }
        else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                /**
                 * the onComplete method is used to show the status of the task it's invoked on,
                 * whether it's successful or not.
                 * @param task the registration task
                 */
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){ //registration failed
                        Toast.makeText(getActivity(), "Registration Problem", Toast.LENGTH_LONG).show();
                    }
                    else {
                        FirebaseAuth.getInstance().signOut();
                        ((AuthenticationActivity)getActivity()).getViewPager().setCurrentItem(0); //to return back to the login tab
                    }
                }
            });
        }
    }
}
