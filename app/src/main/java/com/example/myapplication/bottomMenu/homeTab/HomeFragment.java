package com.example.myapplication.bottomMenu.homeTab;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.database.Deck.Deck;
import com.example.myapplication.database.Deck.DeckViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
//https://www.codeproject.com/Articles/1277308/Work-with-Database-using-Room-and-recyclerview-in

    private DeckViewModel mViewModel;
    private List<Deck> mDataItemList;
    private HomeCustomAdapter mListAdapter;

    private onFragmentInteraction mListener;


//    private Deck deckName = "";

    //the static keyword makes a variable stay throughout all classes, even if the class has been destroyed via garbage collection.

    public void setListData(List<Deck> dataItemList) {
        //if data changed, set new list to adapter of recyclerview

        if (mDataItemList == null) {
            mDataItemList = new ArrayList<Deck>();
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
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        Log.d("TAG", "initDataset: ");

        //get viewmodel
        mViewModel = new ViewModelProvider(this).get(DeckViewModel.class);
        //bind to Livedata
        mViewModel.getAllDecks().observe(this, new Observer<List<Deck>>() {
            @Override
            public void onChanged(@Nullable final List<Deck> dataItems) {
                if (dataItems != null) {
                    setListData(dataItems);
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        Context context = v.getContext();

        // BEGIN_INCLUDE(initializeRecyclerView)
        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewHome);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mListAdapter = new HomeCustomAdapter(mListener);

        if (mDataItemList != null) {
            mListAdapter.setListData(mDataItemList);
        }
        recyclerView.setAdapter(mListAdapter);

        // END_INCLUDE(initializeRecyclerView)

        return v;
    }

    /**
     * Se le asigna un listener al fragment
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (onFragmentInteraction) context;
        if (context instanceof onFragmentInteraction) {
            mListener = (onFragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface onFragmentInteraction {
        public void onListClickListener(Deck dataItem);
    }


}
