package com.example.mostafa.pomodoro.Fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mostafa.pomodoro.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

//TODO: make sure everything is loaded correctly on page startup
public class timer extends Fragment {
    private static final long START_TIME_IN_MILLIS = 6000;
    private static final long BREAK_TIME_IN_MILLIS = 300000;

    @BindView(R.id.text_view_countdown)
    TextView mTextViewCountDown;
    @BindView(R.id.button_start_pause)
    Button mButtonStartPause;
    @BindView(R.id.button_reset)
    Button mButtonReset;
    @BindView(R.id.card)
    LinearLayout card;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;
    private boolean onBreak;

    private long mTimeLeftInMillis;
    private long mEndTime;

    public timer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_timer, container, false);
        ButterKnife.bind(this, view);


        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mButtonStartPause.getText().equals("pause")) {
                    pauseTimer();
                    updateButtonsOnPause();
                } else if (mButtonStartPause.getText().equals("start")){
                    startTimer();
                    updateButtonsOnStart();
                } else if (mButtonStartPause.getText().equals("resume")){
                    startTimer();
                    updateButtonsOnResume();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(mButtonReset.getText().equals("stop")){
                   stopTimer();
                   updateButtonsOnStop();
               }
               else if(mButtonReset.getText().equals("done")){
                   doneTimer();
                   updateButtonsOnDone();
               }
            }
        });
        return view;
    }

    private void updateButtonsOnDone() {
        mButtonStartPause.setText("start");
        mButtonReset.setEnabled(false);
    }

    private void updateButtonsOnStop() {
        mButtonStartPause.setText("start");
        mButtonReset.setEnabled(false);
    }

    private void updateButtonsOnResume() {
        mButtonStartPause.setText("pause");
        mButtonReset.setText("stop");
        mButtonReset.setEnabled(true);
    }

    private void updateButtonsOnStart() {
        mButtonStartPause.setText("pause");
        mButtonReset.setText("stop");
        mButtonReset.setEnabled(true);
    }

    private void updateButtonsOnPause() {
        mButtonStartPause.setText("resume");
        mButtonReset.setText("done");
    }

    private void paintBackground() {
        if(onBreak==false){
            card.setBackgroundColor(Color.argb(255, 69, 203, 133));
        }else{
            card.setBackgroundColor(Color.argb(255, 248, 90, 62));
        }
    }



    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
               doneTimer();
                mTimerRunning = false;
                updateButtonsOnDone();
                mButtonStartPause.setText("start");
                mButtonReset.setEnabled(false);
            }
        }.start();

        mTimerRunning = true;
        updateButtonsOnStart();
    }

    private void stopTimer(){
        paintBackground();
        resetTimer();
        pauseTimer();
        updateButtonsOnStop();
    }

    private void doneTimer() {
        onBreak=!onBreak;
        paintBackground();
        resetTimer();
        pauseTimer();
        updateButtonsOnDone();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;

    }

    private void resetTimer() {
        if(onBreak==false){
            mTimeLeftInMillis = BREAK_TIME_IN_MILLIS;
        }else{
            mTimeLeftInMillis = START_TIME_IN_MILLIS; //letting the timer be 5 min
        }
        updateCountDownText();

    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);
        editor.putBoolean("onBreak", onBreak);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("prefs", MODE_PRIVATE);

        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        onBreak = prefs.getBoolean("onBreak", false);

        updateCountDownText();
        mButtonStartPause.setText("start");

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
             //   updateButtons();
            } else {
                startTimer();
            }
        }
        if(onBreak){
            card.setBackgroundColor(Color.argb(255, 69, 203, 133));
        }
    }

}
