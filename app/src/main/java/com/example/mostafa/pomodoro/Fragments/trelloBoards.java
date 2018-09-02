package com.example.mostafa.pomodoro.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.mostafa.pomodoro.Activities.BottomNavigatorActivity;
import com.example.mostafa.pomodoro.Model.TrelloBoard;
import com.example.mostafa.pomodoro.Presenter.Presenter_Boards;
import com.example.mostafa.pomodoro.R;

import org.jdeferred.DoneCallback;
import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.mostafa.pomodoro.Model.TrelloBoard.parseJSONArrayIntoBoards;

@SuppressLint("ValidFragment")
public class trelloBoards extends Fragment {

    private static final String TAG = "trelloBoards";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView_boards;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    Presenter_Boards presenter;
    String token;

    @SuppressLint("ValidFragment")
    public trelloBoards(String token) {
        this.token=token;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trello_boards, null);
        ButterKnife.bind(this, view);

        //Trying to set the selected item from the bottom navigator from here
        if(((BottomNavigatorActivity)getActivity()).bottomNavigationView.getSelectedItemId()!=R.id.action_boards){
            ((BottomNavigatorActivity)getActivity()).bottomNavigationView.setSelectedItemId(R.id.action_boards);
            //pop one fragment from the back stack because it gets duplicated
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.popBackStack();
        }

        presenter =new Presenter_Boards(this, getContext().getApplicationContext());
        startProgressBar();
        loadBoards();
        return view;
    }

    private void startProgressBar() {
        recyclerView_boards.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

    }

    private void loadBoards() {

        presenter.getNetwork().getBoards(token).done(new DoneCallback<JSONArray>() {
            @Override
            public void onDone(JSONArray result) {
                ArrayList<TrelloBoard> boards = parseJSONArrayIntoBoards(result);
                presenter.setItems(getContext().getApplicationContext(), boards);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
                getRecyclerView_boards().setLayoutManager(mLayoutManager);
                recyclerView_boards.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public RecyclerView getRecyclerView_boards() {
        return recyclerView_boards;
    }



    public void goToMain() {
        ((BottomNavigatorActivity)getActivity()).loadFragment(new trelloLogin());
    }

}
