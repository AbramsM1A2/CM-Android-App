package com.example.myapplication.bottomMenu.homeTab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.ReviewCardsActivity;
import com.example.myapplication.database.Card.CardViewModel;
import com.example.myapplication.database.Deck.Deck;
import com.example.myapplication.database.Deck.DeckViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements HomeCustomAdapter.onDeckListener {
//https://www.codeproject.com/Articles/1277308/Work-with-Database-using-Room-and-recyclerview-in

    ArrayList<Deck> mDataItemList;
    private HomeCustomAdapter mListAdapter;

    private HomeCustomAdapter.onDeckListener mListener;

    private TextView textView;
    private RecyclerView recyclerView;

    private SharedPreferences sharedPreferences;
    private TextView mMessageToAlias;
    private String mUserAlias;
    private String mHelloToAlias1;
    private String mHelloToAlias2;

    //the static keyword makes a variable stay throughout all classes, even if the class has been destroyed via garbage collection.

    public void setListData(List<Deck> dataItemList) {
        //if data changed, set new list to adapter of recyclerview

        if (mDataItemList == null) {
            mDataItemList = new ArrayList<>();
        }
        mDataItemList.clear();
        mDataItemList.addAll(dataItemList);

        if (mListAdapter != null) {
            mListAdapter.setListData(dataItemList);
        }

    }


    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("PREFERENCIAS", MODE_PRIVATE);
        mUserAlias = sharedPreferences.getString("alias","Set your alias");
        mHelloToAlias1 = getContext().getString(R.string.messageToAliasPart1);
        mHelloToAlias2 = getContext().getString(R.string.messageToAliasPart2);

        DeckViewModel mViewModel = new ViewModelProvider(this).get(DeckViewModel.class);

        mViewModel.getDecksCurrentDate(new Date()).observe(this, decks -> {
            //TODO revisar control de la UI para el mazo
            if (decks != null || decks.size() != 0) {
                setListData(decks);
                textView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                setListData(new ArrayList<>());
                textView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        Context context = v.getContext();
        if(!(mUserAlias.equals("Set your alias"))){
            mMessageToAlias = v.findViewById(R.id.messageToAlias);
            mMessageToAlias.setText(mHelloToAlias1+" "+mUserAlias+", "+mHelloToAlias2);

        }


        textView = v.findViewById(R.id.noDeckFound);

        // BEGIN_INCLUDE(initializeRecyclerView)
        recyclerView = v.findViewById(R.id.recyclerViewHome);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mListAdapter = new HomeCustomAdapter(mDataItemList, this);


        if (mDataItemList != null) {
            mListAdapter.setListData(mDataItemList);
        }
        recyclerView.setAdapter(mListAdapter);

        // END_INCLUDE(initializeRecyclerView)
        return v;
    }



    @Override
    public void onListClickListener(int position) {
        CardViewModel mCardViewModel = new ViewModelProvider(this).get(CardViewModel.class);
        AtomicInteger deckSize = new AtomicInteger();
        AtomicReference<Integer> flag = new AtomicReference<>(0);
        Deck dataItem = mDataItemList.get(position);
        mCardViewModel.getAllCardsWithThisId(dataItem.getId()).observe(this, cards -> {
            deckSize.set(cards.size());
            if (flag.get() ==0) {
                if (deckSize.get() < 20) {
                    Toast.makeText(getContext(), R.string.minimun_deck_size, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getContext(), ReviewCardsActivity.class);
                    intent.putExtra("selected_deck_id", String.valueOf(dataItem.getId()));
                    intent.putExtra("selected_deck_name", dataItem.getNameText());

                    startActivity(intent);
                }
                flag.set(1);
            }
        });
    }
}
