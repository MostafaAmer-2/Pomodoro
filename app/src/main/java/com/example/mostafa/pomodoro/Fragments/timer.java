package com.example.mostafa.pomodoro.Fragments;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mostafa.pomodoro.Activities.BottomNavigator;
import com.example.mostafa.pomodoro.Activities.MainActivity;
import com.example.mostafa.pomodoro.Model.TODOitem;
import com.example.mostafa.pomodoro.Presenter.Presenter_timer;
import com.example.mostafa.pomodoro.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class timer extends Fragment {
    private static final String TAG = "timer";
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
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.recycler_view2)
    RecyclerView recyclerView2;
    @BindView(R.id.add_btn)
    Button addBtn;
    @BindView(R.id.item_edit_text)
    EditText itemEditText;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;
    private boolean onBreak;

    private long mTimeLeftInMillis;
    private long mEndTime;


    NotificationCompat.Builder notification;
    private static final int id =45612;

    Presenter_timer presenter;

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

        presenter=new Presenter_timer(this, getActivity().getApplicationContext());

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

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onAddBtnClicked();
            }
        });

        notification = new NotificationCompat.Builder(getActivity().getApplicationContext());
        notification.setAutoCancel(true);

        return view;
    }

    private void popNotification() {
        notification.setSmallIcon(R.drawable.pomodoro);
        notification.setTicker("Pomodoro done");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("It's Break Time");
        notification.setContentText("Reward yourself with a break");
        notification.setDefaults(Notification.DEFAULT_SOUND);

        Intent intent = new Intent(getActivity().getApplicationContext(), BottomNavigator.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity().getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);


        //Builds notification and issues it
        NotificationManager nm = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        nm.notify(id, notification.build());
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
        if(onBreak){
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
            }
        }.start();

        mTimerRunning = true;
        updateButtonsOnStart();
    }

    private void stopTimer(){
        paintBackground();
        resetTimerOnStop();
        pauseTimer();
        updateButtonsOnStop();
    }

    private void doneTimer() {
        resetTimer();
        pauseTimer();
        updateState();
        paintBackground();
        updateButtonsOnDone();
        popNotification();
    }

    private void updateState() {
        if(mTimeLeftInMillis==BREAK_TIME_IN_MILLIS){
            onBreak=true;
        }
        else if (mTimeLeftInMillis==START_TIME_IN_MILLIS){
            onBreak=false;
        }
        else{
            Log.i("Notification", "updateState: none");
        }
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;

    }

    private void resetTimer() {
        if(!onBreak){
            mTimeLeftInMillis = BREAK_TIME_IN_MILLIS;
        }else{
            mTimeLeftInMillis = START_TIME_IN_MILLIS; //letting the timer be 5 min
        }
        updateCountDownText();
    }

    private void resetTimerOnStop() {
        Log.i(TAG, "resetTimerOnStop: "+onBreak);
        if(onBreak){
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

        Log.i(TAG, "onStart: "+ mTimerRunning);

        if (true) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
            Log.i(TAG, "onStart: "+ mTimeLeftInMillis);

            if (mTimeLeftInMillis <= 0) {
                Log.i(TAG, "onStart: in here");
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                resetTimer();
//                pauseTimer();
                updateState();
                paintBackground();
                updateButtonsOnDone();
            } else {
                startTimer();
            }
        }
        if(onBreak){
            card.setBackgroundColor(Color.argb(255, 69, 203, 133));
        }
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public RecyclerView getRecyclerView2() {
        return recyclerView2;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public Button getAddBtn() {
        return addBtn;
    }

    public void setAddBtn(Button addBtn) {
        this.addBtn = addBtn;
    }

    public EditText getItemEditText() {
        return itemEditText;
    }

    public void setItemEditText(EditText itemEditText) {
        this.itemEditText = itemEditText;
    }

    public void updateBtnText(Button btn, TODOitem itemSelected) {
        btn.setText(itemSelected.getPomodoros()+"");
    }
}
