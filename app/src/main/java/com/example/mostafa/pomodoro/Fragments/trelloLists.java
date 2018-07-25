package com.example.mostafa.pomodoro.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.mostafa.pomodoro.Model.TrelloList;
import com.example.mostafa.pomodoro.Presenter.Presenter_Boards;
import com.example.mostafa.pomodoro.Presenter.Presenter_Lists;
import com.example.mostafa.pomodoro.R;

import org.jdeferred.DoneCallback;
import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class trelloLists extends Fragment {

    private static final String TAG = "trelloLists";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    //TODO: here


    Presenter_Lists presenter;
    String token;

    @SuppressLint("ValidFragment")
    public trelloLists() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trello_lists, null);
        ButterKnife.bind(this, view);

        presenter =new Presenter_Lists(this, getContext().getApplicationContext());

     //   recyclerView.setVisibility(View.INVISIBLE);
     //   progressBar.setVisibility(View.VISIBLE);




        ArrayList<TrelloList> lists=new ArrayList<TrelloList>();
        lists.add(new TrelloList("list1","1a"));
        lists.add(new TrelloList("list2","2a"));
        lists.add(new TrelloList("list3","3a"));
        presenter.setItems(getContext().getApplicationContext(), lists);
      //  RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
      //  getRecyclerView().setLayoutManager(mLayoutManager);
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }



  //  public void goToMain() {
  //      ((BottomNavigator)getActivity()).loadFragment(new trelloLogin());
  //  }

}
