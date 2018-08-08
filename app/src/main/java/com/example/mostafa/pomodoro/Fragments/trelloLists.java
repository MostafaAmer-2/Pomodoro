package com.example.mostafa.pomodoro.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.mostafa.pomodoro.Model.TrelloList;
import com.example.mostafa.pomodoro.Presenter.Presenter_Lists;
import com.example.mostafa.pomodoro.R;

import org.jdeferred.DoneCallback;
import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.mostafa.pomodoro.Model.TrelloList.parseJSONArrayIntoLists;

@SuppressLint("ValidFragment")
public class trelloLists extends Fragment {

    private static final String TAG = "trelloLists";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView_lists;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;


    Presenter_Lists presenter;
    String token;
    String boardID;

    @SuppressLint("ValidFragment")
    public trelloLists(String token, String boardID) {
        this.boardID=boardID;
        this.token=token;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trello_lists, null);
        ButterKnife.bind(this, view);

        presenter =new Presenter_Lists(this, getContext().getApplicationContext());
        startProgressBar();
        loadLists();
        return view;
    }

    private void loadLists() {
        presenter.getNetwork().getLists(token,boardID).done(new DoneCallback<JSONArray>() {
            @Override
            public void onDone(JSONArray result) {
                ArrayList<TrelloList> lists = parseJSONArrayIntoLists(result);
                presenter.setItems(getContext().getApplicationContext(), lists);
                recyclerView_lists.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void startProgressBar() {
        recyclerView_lists.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public RecyclerView getRecyclerView_lists() {
        return recyclerView_lists;
    }

}
