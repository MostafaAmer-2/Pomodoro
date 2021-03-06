package com.dreidev.mostafa.pomodoro.Model;

import com.google.firebase.database.DataSnapshot;

import io.realm.RealmObject;

public class TODOitem extends RealmObject {
    private String description;
    private boolean done;
    private int pomodoros;

    public TODOitem() {
        this.done = false;
        this.pomodoros = 1;
    }

    public TODOitem(String title, boolean isDone, int pomodoros) {
        this.description = title;
        this.done = isDone;
        this.pomodoros = pomodoros;
    }

    public TODOitem(String title) {
        this.description = title;
        this.done = false;
        this.pomodoros = 1;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getPomodoros() {
        return pomodoros;
    }

    public static TODOitem convertToItem(DataSnapshot snapshot) {
        if (snapshot.child("pomodoros").getValue() != null)
            return new TODOitem(snapshot.getKey(), Boolean.parseBoolean(snapshot.child("isDone").getValue().toString()), Integer.parseInt(snapshot.child("pomodoros").getValue().toString()));
        else
            return new TODOitem(snapshot.getKey(), Boolean.parseBoolean(snapshot.child("isDone").getValue().toString()), 1);

    }

    public static void increasePomododro(TODOitem item) {
        item.pomodoros = item.pomodoros + 1;
    }

    public static void decreasePomododro(TODOitem item) {
        if (item.pomodoros >= 1)
            item.pomodoros = item.pomodoros - 1;
        else {
            item.pomodoros = 0;
        }
    }

    public static void setPomodorosZero(TODOitem item) {
            item.pomodoros = 0;
    }

    public static void markDone(TODOitem item) {
        item.done = true;
    }
}
