package com.example.mostafa.pomodoro.Activities;

import android.os.Bundle;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mostafa.pomodoro.Model.TrelloBoard;
import com.example.mostafa.pomodoro.R;

import org.jdeferred.Deferred;
import org.jdeferred.DoneCallback;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends Activity {
    RequestQueue requestQueue;
    ArrayList<TrelloBoard> boards1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this); // 'this' is the Context
        testVolley().done(new DoneCallback<JSONArray>() {
            @Override
            public void onDone(JSONArray result) {
                ArrayList<TrelloBoard> boards = parseJSONArrayIntoBoards(result);
                for (TrelloBoard board:boards) {
                   //
                }
                boards1 = boards;
                Log.i("SASA", "onCreate: "+boards1.get(0).getName());
            }
        });


        final ImageView image = (ImageView)findViewById(R.id.imageView);
        final Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.clockwise);
        image.startAnimation(animation1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                image.startAnimation(animation1);
            }
        }, 10000);
    }
    private Promise<JSONArray, VolleyError, Double> testVolley() {
        final Deferred<JSONArray, VolleyError, Double> deferred = new DeferredObject<>();
        String url = "https://api.trello.com/1/members/me/boards?key=51eb6eb13ad2f6cc5bcb87fc923ea427&token=e1b65847d9ccf7eeaad356d340524e680af9c6efdad6babc969b33735151b348";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("SASA", "onResponse: "+response.toString());
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
        requestQueue.add(jsonArrayRequest);
        return deferred.promise();
    }

    private ArrayList<TrelloBoard> parseJSONArrayIntoBoards(JSONArray response) {
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

    public void clockwise(View view){
        ImageView image = (ImageView)findViewById(R.id.imageView);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.clockwise);
        image.startAnimation(animation1);

    }

    public void zoom(View view){

        ImageView image = (ImageView)findViewById(R.id.imageView);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.myanimation);
        image.startAnimation(animation);
    }

    public void fade(View view){
        ImageView image = (ImageView)findViewById(R.id.imageView);
        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.fade);
        image.startAnimation(animation1);
    }

    public void blink(View view){
        ImageView image = (ImageView)findViewById(R.id.imageView);
        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
        image.startAnimation(animation1);
    }

    public void move(View view){
        ImageView image = (ImageView)findViewById(R.id.imageView);
        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
        image.startAnimation(animation1);
    }

    public void slide(View view){
        ImageView image = (ImageView)findViewById(R.id.imageView);
        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide);
        image.startAnimation(animation1);
    }

}
