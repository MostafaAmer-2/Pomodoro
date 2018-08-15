package com.example.mostafa.pomodoro.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.RealmObject;

public class TrelloBoard extends RealmObject {
    private String name;
    private String id;

    public TrelloBoard(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public TrelloBoard(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public static ArrayList<TrelloBoard> parseJSONArrayIntoBoards(JSONArray response) {
        ArrayList<TrelloBoard> boards = new ArrayList<>();
        for (int i = 0; i<response.length(); i++)
            try {
                JSONObject firstObject = response.getJSONObject(i);
                String name = firstObject.getString("name");
                String id = firstObject.getString("id");
                TrelloBoard board = new TrelloBoard(name, id);
                boards.add(board);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        return boards;
    }
}
