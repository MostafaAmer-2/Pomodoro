package com.example.mostafa.pomodoro.Fragments;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
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

import com.example.mostafa.pomodoro.Activities.BottomNavigatorActivity;
import com.example.mostafa.pomodoro.Model.TODOitem;
import com.example.mostafa.pomodoro.Presenter.Presenter_TODOitems;
import com.example.mostafa.pomodoro.Presenter.Presenter_Timer;
import com.example.mostafa.pomodoro.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.mostafa.pomodoro.Model.TODOitem.decreasePomododro;
import static com.example.mostafa.pomodoro.Model.TODOitem.markDone;

public class TimerFragment extends Fragment {
    private static final String TAG = "TimerFragment";
    private static final long START_TIME_IN_MILLIS = 6000;
    private static final long BREAK_TIME_IN_MILLIS = 300000;

    @BindView(R.id.text_view_countdown)
    TextView mTextViewCountDown;
    @BindView(R.id.button_start_pause)
    Button mButtonStartPause;
    @BindView(R.id.button_reset_done)
    Button mButtonResetDone;
    @BindView(R.id.card)
    LinearLayout timerCard;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView_todoList;
    @BindView(R.id.recycler_view2)
    RecyclerView recyclerView_doneList;
    @BindView(R.id.add_btn)
    Button addBtn;
    @BindView(R.id.item_edit_text)
    EditText itemNameField;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private boolean onBreak;
    private long mTimeLeftInMillis;
    private long mEndTime;
    private NotificationCompat.Builder notification;
    private static final int notificationID = 45612;
    private Presenter_TODOitems presenter_todos;
    private Presenter_Timer presenter_timer;
    private SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);
        ButterKnife.bind(this, view);
        presenter_todos = new Presenter_TODOitems(this, getActivity().getApplicationContext());
        presenter_timer = new Presenter_Timer(this, getActivity().getApplicationContext());

        createNotification();
        setOnClickListeners();
        return view;
    }

    private void setOnClickListeners() {
        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mButtonStartPause.getText().equals("pause")) {
                    presenter_timer.pauseTimer();
                    presenter_timer.updateButtonsOnPause();
                } else if (mButtonStartPause.getText().equals("start")) {
                    presenter_timer.startTimer();
                    presenter_timer.updateButtonsOnStart();
                } else if (mButtonStartPause.getText().equals("resume")) {
                    presenter_timer.startTimer();
                    presenter_timer.updateButtonsOnResume();
                }
            }
        });

        mButtonResetDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mButtonResetDone.getText().equals("stop")) {
                    presenter_timer.stopTimer();
                    presenter_timer.updateButtonsOnStop();
                } else if (mButtonResetDone.getText().equals("done")) {
                    presenter_timer.doneTimer();
                    presenter_timer.updateButtonsOnDone();
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter_todos.onAddBtnClicked();
            }
        });
    }


    private void createNotification() {
        notification = new NotificationCompat.Builder(getActivity().getApplicationContext());
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.drawable.pomodoro);
        notification.setTicker("Pomodoro done");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("It's Break Time");
        notification.setContentText("Reward yourself with a break");
        notification.setDefaults(Notification.DEFAULT_SOUND);

        Intent intent = new Intent(getActivity().getApplicationContext(), BottomNavigatorActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity().getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

    }


    public void paintBackground() {
        if (onBreak) {
            timerCard.setBackgroundColor(Color.argb(255, 69, 203, 133));
        } else {
            timerCard.setBackgroundColor(Color.argb(255, 248, 90, 62));
        }
    }


    public void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    //TODO: onStop and onStart methods may need more refactoring
    @Override
    public void onStop() {
        super.onStop();
        uploadToSharedPreferences();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
//        presenter_todos.getNetwork().onCloseUpdateCache();
    }

    private void uploadToSharedPreferences() {
        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);
        editor.putBoolean("onBreak", onBreak);

        editor.apply();
    }

    @Override
    public void onStart() {
        super.onStart();

        prefs = getActivity().getApplicationContext().getSharedPreferences("prefs", MODE_PRIVATE);
        downloadFromSharedPreferences();
        updateCountDownText();
//        mButtonStartPause.setText("start");
        presenter_timer.checkIfTimerFinished();
        paintBackground();
    }

    private void downloadFromSharedPreferences() {
        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        onBreak = prefs.getBoolean("onBreak", false);
    }

    public RecyclerView getRecyclerView_todoList() {
        return recyclerView_todoList;
    }

    public RecyclerView getRecyclerView_doneList() {
        RecyclerView rec = this.recyclerView_doneList;

        return rec;
    }

    public void updateBtnText(Button btn, TODOitem itemSelected) {
        btn.setText(itemSelected.getPomodoros() + "");
    }

    public EditText getItemNameField() {
        return itemNameField;
    }


    public static String getTAG() {
        return TAG;
    }

    public static long getStartTimeInMillis() {
        return START_TIME_IN_MILLIS;
    }

    public static long getBreakTimeInMillis() {
        return BREAK_TIME_IN_MILLIS;
    }

    public Button getmButtonStartPause() {
        return mButtonStartPause;
    }

    public Button getmButtonResetDone() {
        return mButtonResetDone;
    }

    public CountDownTimer getmCountDownTimer() {
        return mCountDownTimer;
    }

    public boolean isOnBreak() {
        return onBreak;
    }

    public long getmTimeLeftInMillis() {
        return mTimeLeftInMillis;
    }

    public NotificationCompat.Builder getNotification() {
        return notification;
    }

    public static int getNotificationID() {
        return notificationID;
    }

    public Presenter_TODOitems getPresenter_todos() {
        return presenter_todos;
    }

    public Presenter_Timer getPresenter_timer() {
        return presenter_timer;
    }

    public void setmCountDownTimer(CountDownTimer mCountDownTimer) {
        this.mCountDownTimer = mCountDownTimer;
    }

    public void setmTimerRunning(boolean mTimerRunning) {
        this.mTimerRunning = mTimerRunning;
    }

    public void setOnBreak(boolean onBreak) {
        this.onBreak = onBreak;
    }

    public void setmTimeLeftInMillis(long mTimeLeftInMillis) {
        this.mTimeLeftInMillis = mTimeLeftInMillis;
    }

    public void setmEndTime(long mEndTime) {
        this.mEndTime = mEndTime;
    }

    public boolean ismTimerRunning() {
        return mTimerRunning;
    }

    public SharedPreferences getPrefs() {
        return prefs;
    }

    public long getmEndTime() {
        return mEndTime;
    }


}