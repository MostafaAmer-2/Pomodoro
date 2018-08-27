package com.example.mostafa.pomodoro.Fragments;


import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mostafa.pomodoro.Activities.BottomNavigatorActivity;
import com.example.mostafa.pomodoro.Auth.GoogleAuth;
import com.example.mostafa.pomodoro.R;
import com.example.mostafa.pomodoro.Settings.Preferences;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    String TAG = "login_page";
    @BindView(R.id.emailField)
    EditText emailField;
    @BindView(R.id.passwordField)
    EditText passwordField;
    @BindView(R.id.loginBtn)
    Button loginBtn;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.googleSignIn)
    SignInButton googleBtn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleAuth gAuth;

    public LoginFragment() {
        // Required empty public constructor
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        mAuth = FirebaseAuth.getInstance();

        gAuth = new GoogleAuth(getActivity().getApplicationContext());

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            String email = extras.getString("email");
            String password = extras.getString("password");
            Log.i(TAG, "onCreate: " + email);
            emailField.setText(email);
            passwordField.setText(password);
        }
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            /**
             * the purpose of this method is to detect the change in state of the authentication, whether the user
             * gets signed in, signed out, ..etc
             * @param firebaseAuth Firebase Authentication Instance
             */
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) { //if there exists a user who is signed in right now
                    Log.i(TAG, "onAuthStateChanged: USER JUST SIGNED IN WITH GOOGLE"+firebaseAuth.getCurrentUser().getUid());
                     Preferences.saveUserID(getActivity().getApplicationContext(), firebaseAuth.getCurrentUser().getUid());
                     goToMain();
                }
            }
        };

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });
        return view;
    }

    private void signInWithGoogle() {
        Intent signInIntent = gAuth.mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, gAuth.RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == gAuth.RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            gAuth.handleSignInResult(task);
        }
    }

    private void goToMain(){
        Intent go_to_main = new Intent(getActivity(), BottomNavigatorActivity.class);
        startActivity(go_to_main);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginBtn.setOnClickListener(new View.OnClickListener() {

            /**
             * Calling the startSignIn method once the login button gets clicked
             * @param view the biew containing the button
             */
            @Override
            public void onClick(View view) {
                startSignIn();
            }
        });
    }

    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    /**
     * Make sure the user gets signed out, as the activity stops
     */
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
            FirebaseAuth.getInstance().signOut();
        }
    }

    /**
     * startSignIn method collects the email and password of the user and tries to sign them in
     * after checking that they meet the preliminary checks. In case of any error, a toast is shown to the user.
     */
    private void startSignIn() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) { //condition to ensure the fields are not left empty
            Toast.makeText(getActivity(), "Empty Fields", Toast.LENGTH_LONG).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                /**
                 * the onComplete method is used to show the status of the task it's invoked on,
                 * whether it's successful or not.
                 * @param task the sign in task
                 */
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) { //sign in failed
                        Toast.makeText(getActivity(), "Sign In Problem", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
