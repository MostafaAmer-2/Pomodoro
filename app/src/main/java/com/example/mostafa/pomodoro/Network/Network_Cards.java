package com.example.mostafa.pomodoro.Network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mostafa.pomodoro.Model.TODOitem;
import com.example.mostafa.pomodoro.Presenter.Presenter_Cards;
import com.example.mostafa.pomodoro.Presenter.Presenter_Lists;
import com.example.mostafa.pomodoro.Settings.Preferences;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jdeferred.Deferred;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;
import org.json.JSONArray;
import org.json.JSONObject;

public class Network_Cards {

    private DatabaseReference dref;
    private DatabaseReference itemsRef;

    private RequestQueue requestQueue;
    private Presenter_Cards presenter;
    private Context ctx;

    public Network_Cards(Presenter_Cards presenter, Context ctx){
        requestQueue = Volley.newRequestQueue(ctx);
        this.presenter=presenter;
        this.ctx=ctx;
        dref = FirebaseDatabase.getInstance().getReference(Preferences.loadUserID(ctx));
        itemsRef = dref.child("items");
    }

    public Promise<JSONArray, VolleyError, Double> getCards(String token, String listID) {
        final Deferred<JSONArray, VolleyError, Double> deferred = new DeferredObject<>();
        String url = "https://api.trello.com/1/lists/"+listID+"/cards?key=51eb6eb13ad2f6cc5bcb87fc923ea427&token="+token;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        deferred.resolve(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        deferred.reject(error);
                       if(error.networkResponse!= null && error.networkResponse.statusCode== 400 || error.networkResponse.statusCode== 401 ){
                           //   presenter.getListsFrag().goToMain();
                       }
                    }
                });
        //add request to queue
        requestQueue.add(jsonArrayRequest); //adding the request to the requestQueue in the network
        return deferred.promise();
    }

    public Promise<JSONObject, VolleyError, Double> addCard(String token, String listID, String cardName) {
        final Deferred<JSONObject, VolleyError, Double> deferred = new DeferredObject<>();
        String url = "https://api.trello.com/1/cards?name="+cardName+"&idList="+ listID+
                "&keepFromSource=all&key=51eb6eb13ad2f6cc5bcb87fc923ea427&token="+token;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        deferred.resolve(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        deferred.reject(error);
                    }
                });
        //add request to queue
        requestQueue.add(jsonObjectRequest); //adding the request to the requestQueue in the network
        return deferred.promise();
    }

    public Promise<JSONArray, VolleyError, Double> testTokenValid(String token, String listID) { //to be used in the splash screen
        final Deferred<JSONArray, VolleyError, Double> deferred = new DeferredObject<>();
        String url = "https://api.trello.com/1/boards/"+listID+"/lists?key=51eb6eb13ad2f6cc5bcb87fc923ea427&token="+token;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        deferred.resolve(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        deferred.reject(error);
                        if(error.networkResponse!= null && error.networkResponse.statusCode== 400 || error.networkResponse.statusCode== 401 ) {
                            SharedPreferences sharedPreferences=ctx.getSharedPreferences("sharedPrefs",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("checkTokenValidityAndUpdatePrefrences", "false");
                            editor.commit();
                        }
                    }
                });
        //add request to queue
        requestQueue.add(jsonArrayRequest); //adding the request to the requestQueue in the network
        return deferred.promise();
    }

    public Presenter_Cards getPresenter() {
        return presenter;
    }

    public void setPresenter(Presenter_Cards presenter) {
        this.presenter = presenter;
    }

    public Promise<String, String, Double> addItem(TODOitem newItem) {
        final Deferred<String, String, Double> deferred = new DeferredObject<>();
        try{
            String nodeName= newItem.getDescription();
            nodeName = nodeName.replace(".","");
            nodeName = nodeName.replace("#","");
            nodeName = nodeName.replace("$","");
            nodeName = nodeName.replace("[","");
            nodeName = nodeName.replace("]","");
            itemsRef.child(nodeName).child("isDone").setValue(newItem.isDone());
            itemsRef.child(nodeName).child("pomodoros").setValue(newItem.getPomodoros());
            deferred.resolve("Item added");
        }
        catch(Error error){
            deferred.reject("Item not added");
        }
        return deferred.promise();
    }
}
