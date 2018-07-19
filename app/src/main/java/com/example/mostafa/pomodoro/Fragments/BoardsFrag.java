package com.example.mostafa.pomodoro.Fragments;

import android.annotation.SuppressLint;
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

import com.example.mostafa.pomodoro.Activities.BottomNavigator;
import com.example.mostafa.pomodoro.Model.TrelloBoard;
import com.example.mostafa.pomodoro.Presenter.Presenter;
import com.example.mostafa.pomodoro.R;

import org.jdeferred.DoneCallback;
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


        presenter.getNetworkPresenter().getBoards(token).done(new DoneCallback<JSONArray>() {
            @Override
            public void onDone(JSONArray result) {
                ArrayList<TrelloBoard> boards = parseJSONArrayIntoBoards(result);
                presenter.setItems(getContext().getApplicationContext(), boards);
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
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



    public void goToMain() {
        ((BottomNavigator)getActivity()).loadFragment(new MainFrag());
    }

}
