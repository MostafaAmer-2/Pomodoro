package com.example.mostafa.pomodoro.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mostafa.pomodoro.Model.TrelloCard;
import com.example.mostafa.pomodoro.Presenter.Presenter_Cards;
import com.example.mostafa.pomodoro.R;

import org.jdeferred.DoneCallback;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

import static com.example.mostafa.pomodoro.Model.TrelloCard.parseJSONArrayIntoCards;

@SuppressLint("ValidFragment")
public class trelloCards extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView_cards;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;


    private Presenter_Cards presenter;
    private String token;
    private String listID;

    Realm realm;

    @SuppressLint("ValidFragment")
    public trelloCards(String token, String listID) {
        this.listID = listID;
        this.token = token;
        realm = Realm.getDefaultInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trello_cards, null);
        ButterKnife.bind(this, view);

        presenter =new Presenter_Cards(this, getContext().getApplicationContext());
        Toast.makeText(getActivity().getApplicationContext(), "Press a card and hold to add it to TODOs", Toast.LENGTH_SHORT).show();
        startProgressBar();
        loadCards();
        addFloatingActionButtonListener();

        return view;
    }

    private void addFloatingActionButtonListener() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAndPopAlertDialog();
            }
        });

    }

    private void createAndPopAlertDialog(){
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
                    recyclerView_cards.setVisibility(View.INVISIBLE);
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
                                    getRecyclerView_cards().setLayoutManager(mLayoutManager);
                                    recyclerView_cards.setVisibility(View.VISIBLE);
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

    private void loadCards() {
        presenter.getNetwork().getCards(token,listID).done(new DoneCallback<JSONArray>() {
            @Override
            public void onDone(JSONArray result) {
                ArrayList<TrelloCard> cards = parseJSONArrayIntoCards(result);
                presenter.setItems(getContext().getApplicationContext(), cards);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
                getRecyclerView_cards().setLayoutManager(mLayoutManager);
                recyclerView_cards.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void startProgressBar() {
        recyclerView_cards.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public RecyclerView getRecyclerView_cards() {
        return recyclerView_cards;
    }

}
