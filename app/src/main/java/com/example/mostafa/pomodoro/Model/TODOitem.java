package com.example.mostafa.pomodoro.Model;

import com.google.firebase.database.DataSnapshot;

public class TODOitem {
    private String description;
    private boolean done;
    private int pomodoros;

    public TODOitem(String title){
        this.description=title;
        this.done=false;
        this.pomodoros=1;
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

    public void setPomodoros(int pomodoros) {
        this.pomodoros = pomodoros;
    }

    public static TODOitem convertToItem(DataSnapshot snapshot) {
        return new TODOitem(snapshot.getKey());
    }

    public static void increasePomododro(TODOitem item){
        item.pomodoros=item.pomodoros+1;
    }

    public static void decreasePomododro(TODOitem item){
        item.pomodoros=item.pomodoros-1;
    }
}
