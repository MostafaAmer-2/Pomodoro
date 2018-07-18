package com.example.mostafa.pomodoro.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.mostafa.pomodoro.Model.TrelloBoard;
import com.example.mostafa.pomodoro.Presenter.Presenter;
import com.example.mostafa.pomodoro.R;

import org.jdeferred.Deferred;
import org.jdeferred.DoneCallback;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;
import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.mostafa.pomodoro.Model.TrelloBoard.parseJSONArrayIntoBoards;

@SuppressLint("ValidFragment")
public class BoardsFrag extends Fragment {

    private static final String TAG = "BoardsFrag";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    //TODO: here


    Presenter presenter;
    String token;

    @SuppressLint("ValidFragment")
    public BoardsFrag(String token) {
        this.token=token;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, null);
        ButterKnife.bind(this, view);

        presenter =new Presenter(this, getContext().getApplicationContext());

        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        testVolley().done(new DoneCallback<JSONArray>() {
            @Override
            public void onDone(JSONArray result) {
                ArrayList<TrelloBoard> boards = parseJSONArrayIntoBoards(result);
                for (TrelloBoard board:boards) {
                    Log.i(TAG, "Board name: "+board.getName());
                    Log.i(TAG, "Board id: "+board.getId());
                }
                presenter.setItems(getContext().getApplicationContext(), boards);
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                Log.i(TAG, "onCreate: "+boards.get(0).getName());
                Log.i(TAG, "wanted "+presenter.getItems().get(0).getName());
            }
        });





        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    private Promise<JSONArray, VolleyError, Double> testVolley() {
        final Deferred<JSONArray, VolleyError, Double> deferred = new DeferredObject<>();
        String url = "https://api.trello.com/1/members/me/boards?key=51eb6eb13ad2f6cc5bcb87fc923ea427&token="+token;
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
                        //TODO: check on the status code of the error "https://stackoverflow.com/questions/43377326/get-the-http-body-response-of-a-post-with-volley-android"
                    }
                });
        //add request to queue
        presenter.getNetwork().getRequestQueue().add(jsonArrayRequest); //adding the request to the requestQueue in the network
        return deferred.promise();
    }
}
