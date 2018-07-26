package com.example.mostafa.pomodoro.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.mostafa.pomodoro.Activities.BottomNavigator;
import com.example.mostafa.pomodoro.Model.TrelloBoard;
import com.example.mostafa.pomodoro.Model.TrelloCard;
import com.example.mostafa.pomodoro.Presenter.Presenter_Boards;
import com.example.mostafa.pomodoro.Presenter.Presenter_Cards;
import com.example.mostafa.pomodoro.R;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.os.Build.VERSION_CODES.O;
import static com.example.mostafa.pomodoro.Model.TrelloBoard.parseJSONArrayIntoBoards;
import static com.example.mostafa.pomodoro.Model.TrelloCard.parseJSONArrayIntoCards;

@SuppressLint("ValidFragment")
public class trelloCards extends Fragment {

    private static final String TAG = "trelloCards";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    //TODO: here


    private Presenter_Cards presenter;
    private String token;
    private String listID;

    @SuppressLint("ValidFragment")
    public trelloCards(String token, String listID) {
        this.listID = listID;
        this.token = token;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trello_cards, null);
        ButterKnife.bind(this, view);

        presenter =new Presenter_Cards(this, getContext().getApplicationContext());

        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        presenter.getNetwork().getCards(token,listID).done(new DoneCallback<JSONArray>() {
            @Override
            public void onDone(JSONArray result) {
                ArrayList<TrelloCard> cards = parseJSONArrayIntoCards(result);
                presenter.setItems(getContext().getApplicationContext(), cards);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
                getRecyclerView().setLayoutManager(mLayoutManager);
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder= new AlertDialog.Builder(getActivity());
                View mView=getLayoutInflater().inflate(R.layout.dialog_add_card,null);
                final EditText cardName=(EditText) mView.findViewById(R.id.cardName);
                Button addCardBtn= (Button) mView.findViewById(R.id.addCardBtn);

                mBuilder.setView(mView);
                final AlertDialog dialog=mBuilder.create();
                dialog.show();

                addCardBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!cardName.getText().toString().isEmpty()){
                            recyclerView.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.VISIBLE);
                            presenter.getNetwork().addCard(token,listID,cardName.getText().toString()).done(new DoneCallback<JSONObject>() {
                                @Override
                                public void onDone(JSONObject result) {
                                    presenter.getNetwork().getCards(token,listID).done(new DoneCallback<JSONArray>() {
                                        @Override
                                        public void onDone(JSONArray result) {
                                            ArrayList<TrelloCard> cards = parseJSONArrayIntoCards(result);
                                            presenter.setItems(getContext().getApplicationContext(), cards);
                                            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
                                            getRecyclerView().setLayoutManager(mLayoutManager);
                                            recyclerView.setVisibility(View.VISIBLE);
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    });
                                }
                            });
                            dialog.dismiss();
                        }else{
                            Toast.makeText(getActivity().getApplicationContext(), "Please Insert a Name", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
        ((BottomNavigator)getActivity()).loadFragment(new trelloLogin());
    }

}
