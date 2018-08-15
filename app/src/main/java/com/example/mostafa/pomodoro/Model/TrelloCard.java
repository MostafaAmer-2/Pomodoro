package com.example.mostafa.pomodoro.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.RealmObject;

public class TrelloCard extends RealmObject {
    private String name;
    private String id;

    public TrelloCard() {

    }

    public TrelloCard(String name, String id) {
        this.name = name;
        this.id=id;
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

    public static ArrayList<TrelloCard> parseJSONArrayIntoCards(JSONArray response){
        ArrayList<TrelloCard> lists = new ArrayList<>();
        for (int i = 0; i<response.length(); i++)
            try {
                JSONObject firstObject = response.getJSONObject(i);
                String name = firstObject.getString("name");
                String id = firstObject.getString("id");
                TrelloCard list = new TrelloCard(name, id);
                lists.add(list);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        return lists;
    }
}
