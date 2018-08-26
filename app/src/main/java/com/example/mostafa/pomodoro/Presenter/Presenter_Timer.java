package com.example.mostafa.pomodoro.Presenter;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;

import com.example.mostafa.pomodoro.Fragments.TimerFragment;
import com.example.mostafa.pomodoro.Model.TODOitem;
import com.example.mostafa.pomodoro.R;
import com.example.mostafa.pomodoro.RecyclerViewAdapter_TODOs;
import com.example.mostafa.pomodoro.Settings.Preferences;

import io.realm.Realm;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.mostafa.pomodoro.Model.TODOitem.decreasePomododro;
import static com.example.mostafa.pomodoro.Model.TODOitem.markDone;

public class Presenter_Timer {
    private TimerFragment timerFragment;
    private Context ctx;
    Realm realm;

    public Presenter_Timer(TimerFragment timerFragment, Context ctx) {
        this.timerFragment = timerFragment;
        this.ctx=ctx;
        realm = Realm.getDefaultInstance();
    }

    public void popNotification() {
        if (timerFragment.isOnBreak()) {
            //Builds notification and issues it
            NotificationManager nm = (NotificationManager) timerFragment.getActivity().getSystemService(NOTIFICATION_SERVICE);
            nm.notify(timerFragment.getNotificationID(), timerFragment.getNotification().build());
        }
    }

    public void updateButtonsOnDone() {
        timerFragment.getmButtonStartPause().setText("start");
        timerFragment.getmButtonResetDone().setEnabled(false);
    }

    public void updateButtonsOnStop() {
        timerFragment.getmButtonStartPause().setText("start");
        timerFragment.getmButtonResetDone().setEnabled(false);
    }

    public void updateButtonsOnResume() {
        timerFragment.getmButtonStartPause().setText("pause");
        timerFragment.getmButtonResetDone().setText("stop");
        timerFragment.getmButtonResetDone().setEnabled(true);
    }

    public void updateButtonsOnStart() {
        timerFragment.getmButtonStartPause().setText("pause");
        timerFragment.getmButtonResetDone().setText("stop");
        timerFragment.getmButtonResetDone().setEnabled(true);
    }

    public void updateButtonsOnPause() {
        timerFragment.getmButtonStartPause().setText("resume");
        timerFragment.getmButtonResetDone().setText("done");
    }

    public void startTimer() {
        timerFragment.setmEndTime(System.currentTimeMillis() + timerFragment.getmTimeLeftInMillis());

        timerFragment.setmCountDownTimer(new CountDownTimer(timerFragment.getmTimeLeftInMillis(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerFragment.setmTimeLeftInMillis(millisUntilFinished);
                timerFragment.updateCountDownText();
            }

            @Override
            public void onFinish() {
                doneTimer();
                timerFragment.setmTimerRunning(false);
            }
        }.start());

        timerFragment.setmTimerRunning(true);
        timerFragment.getPresenter_timer().updateButtonsOnStart();
    }

    public void stopTimer() {
        timerFragment.paintBackground();
        resetTimerOnStop();
        pauseTimer();
        timerFragment.getPresenter_timer().updateButtonsOnStop();
        timerFragment.setmTimerRunning(false);
    }

    public void doneTimer() {
        resetTimer();
        pauseTimer();
        updateState();
        timerFragment.paintBackground();
        timerFragment.getPresenter_timer().updateButtonsOnDone();
        timerFragment.getPresenter_timer().popNotification();
        timerFragment.setmTimerRunning(false);

        //decreasing a pomodoro
        TODOitem currentItem = timerFragment.getPresenter_todos().getCurrentItem();
        RecyclerViewAdapter_TODOs.ViewHolder currentHolder = timerFragment.getPresenter_todos().getCurrentHolder();
        if (currentItem != null && currentHolder!=null && timerFragment.isOnBreak()) {
            realm.beginTransaction();
            decreasePomododro(currentItem);
            realm.commitTransaction();
            timerFragment.getPresenter_todos().getNetwork().updatePomodoros(currentItem);
            timerFragment.updateBtnText(timerFragment.getPresenter_todos().getCurrentHolder().getAdd_pomodoro_btn(), currentItem);
            if (currentItem.getPomodoros() == 0) {
//                timerFragment.getPresenter_todos().getNetwork().removeNode(currentItem.getDescription());


                timerFragment.getPresenter_todos().getCurrentHolder().getMarkDone().performClick();
//                markDone(currentItem);

//                timerFragment.getPresenter_todos().addDoneItem(currentItem);
//                timerFragment.getPresenter_todos().getNetwork().addNode(currentItem.getDescription(), currentItem.isDone(), currentItem.getPomodoros());

//                //resetting everything in the viewHolder
//                timerFragment.getPresenter_todos().setCurrentItem(null);
//                Log.i("NullTest", "doneTimer: "+timerFragment.getPresenter_todos().getCurrentHolder());
//                timerFragment.getPresenter_todos().getCurrentHolder().getParent_layout().setBackgroundColor(ctx.getResources().getColor(R.color.pomodoroBlueTrans));
//                timerFragment.getPresenter_todos().setCurrentHolder(null);
            }
        }
    }


    public void updateState() {
        if (timerFragment.getmTimeLeftInMillis() == timerFragment.getBreakTimeInMillis()) {
            timerFragment.setOnBreak(true);
        } else if (timerFragment.getmTimeLeftInMillis() == timerFragment.getStartTimeInMillis()) {
            timerFragment.setOnBreak(false);
        } else {
            Log.i("Notification", "updateState: none");
        }
    }

    public void pauseTimer() {
        timerFragment.getmCountDownTimer().cancel();
        timerFragment.setmTimerRunning(false);
        timerFragment.setmTimerRunning(true);

    }

    public void resetTimer() {
        if (!timerFragment.isOnBreak()) {
            timerFragment.setmTimeLeftInMillis(timerFragment.getBreakTimeInMillis());
        } else {
            timerFragment.setmTimeLeftInMillis(timerFragment.getStartTimeInMillis()); //letting the TimerFragment be 5 min
        }
        timerFragment.updateCountDownText();
    }

    public void resetTimerOnStop() {
        if (timerFragment.isOnBreak()) {
            timerFragment.setmTimeLeftInMillis(timerFragment.getBreakTimeInMillis());
        } else {
            timerFragment.setmTimeLeftInMillis(timerFragment.getStartTimeInMillis()); //letting the TimerFragment be 5 min
        }
        timerFragment.updateCountDownText();
    }

    public void checkIfTimerFinished() {
        if (timerFragment.ismTimerRunning()) {
            timerFragment.setmEndTime(timerFragment.getPrefs().getLong("endTime", 0));
            timerFragment.setmTimeLeftInMillis(timerFragment.getmEndTime() - System.currentTimeMillis());
            if (timerFragment.getmTimeLeftInMillis() <= 0) {
                timerFragment.setmTimeLeftInMillis(0);
                timerFragment.setmTimerRunning(false);
                timerFragment.getPresenter_timer().resetTimer();
//                pauseTimer();
                timerFragment.getPresenter_timer().updateState();
//                timerFragment.paintBackground(); //Already done in TimerFragment inside onStart method
                timerFragment.getPresenter_timer().updateButtonsOnDone();
            } else {
                timerFragment.getPresenter_timer().startTimer();
            }
        }
    }
}