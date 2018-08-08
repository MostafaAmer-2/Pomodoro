package com.example.mostafa.pomodoro.Network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mostafa.pomodoro.Presenter.Presenter_Boards;
import com.example.mostafa.pomodoro.Presenter.Presenter_Lists;

import org.jdeferred.Deferred;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;
import org.json.JSONArray;

public class Network_Lists {

    private RequestQueue requestQueue;
    private Presenter_Lists presenter;
    private Context ctx;
  //  private String listID;

    public Network_Lists(Presenter_Lists presenter, Context ctx){
        requestQueue = Volley.newRequestQueue(ctx); // 'this' is the Context
        this.presenter=presenter;
    }

    public Promise<JSONArray, VolleyError, Double> getLists(String token, String boardID) {
        final Deferred<JSONArray, VolleyError, Double> deferred = new DeferredObject<>();
        String url = "https://api.trello.com/1/boards/"+boardID+"/lists?key=51eb6eb13ad2f6cc5bcb87fc923ea427&token="+token;
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

    public Promise<JSONArray, VolleyError, Double> testTokenValid(String token, String boardID) { //to be used in the splash screen
        final Deferred<JSONArray, VolleyError, Double> deferred = new DeferredObject<>();
        String url = "https://api.trello.com/1/boards/"+boardID+"/lists?key=51eb6eb13ad2f6cc5bcb87fc923ea427&token="+token;
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

    public Presenter_Lists getPresenter() {
        return presenter;
    }

    public void setPresenter(Presenter_Lists presenter) {
        this.presenter = presenter;
    }
}
