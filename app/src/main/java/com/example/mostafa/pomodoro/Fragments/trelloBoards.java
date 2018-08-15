package com.example.mostafa.pomodoro.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
    Realm realm;

    @SuppressLint("ValidFragment")
    public trelloBoards(String token) {
        this.token=token;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trello_boards, null);
        ButterKnife.bind(this, view);

        // Initialize Realm (just once per application)
        io.realm.Realm.init(getActivity().getApplicationContext());

// Get a Realm instance for this thread
        realm = io.realm.Realm.getDefaultInstance();

        presenter =new Presenter_Boards(this, getContext().getApplicationContext());
        startProgressBar();
        loadBoards();
        return view;
    }

    private void startProgressBar() {
//        recyclerView_boards.setVisibility(View.INVISIBLE);
//        progressBar.setVisibility(View.VISIBLE);

        RealmResults<TrelloBoard> cach = realm.where(TrelloBoard.class).findAll();
        Log.i(TAG, "loadBoards: "+cach.size());
        ArrayList<TrelloBoard> cachedBoards=new ArrayList<TrelloBoard>();
        for(int i=0; i< cach.size();i++){
            cachedBoards.add(cach.get(i));
        }
        presenter.setItems(getContext().getApplicationContext(), cachedBoards);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        getRecyclerView_boards().setLayoutManager(mLayoutManager);
        recyclerView_boards.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);


    }

    private void loadBoards() {

        presenter.getNetwork().getBoards(token).done(new DoneCallback<JSONArray>() {
            @Override
            public void onDone(JSONArray result) {
                ArrayList<TrelloBoard> boards = parseJSONArrayIntoBoards(result);
//                Log.i(TAG, "onDone: "+boards.size());
//                Log.i(TAG, "onDone: "+realm.where(TrelloBoard.class).findAll().size());
                if(boards.size()!=realm.where(TrelloBoard.class).findAll().size()){
                   //updating the cache
                    realm.beginTransaction();
                    realm.deleteAll();
                    for(int i=0; i< boards.size();i++){
                        TrelloBoard tmpBoard= boards.get(i);
                        TrelloBoard realmBoard = realm.copyToRealm(tmpBoard);
                    }
                    realm.commitTransaction();

                    presenter.setCache(getContext().getApplicationContext(), boards);
                    presenter.setItems(getContext().getApplicationContext(), presenter.getCache());
                }
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
