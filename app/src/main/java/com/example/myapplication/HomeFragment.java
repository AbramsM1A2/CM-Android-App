package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.database.Deck.Deck;
import com.example.myapplication.database.Deck.DeckViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private DeckViewModel mDeckViewModel;
    private String deckName = "";

    private static int spinnerValue;
    private static String savedDeck;
    Spinner spinner;

    //the static keyword makes a variable stay throughout all classes, even if the class has been destroyed via garbage collection.


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        //------------Button-------------
        Button b = v.findViewById(R.id.buttonStart);
        b.setOnClickListener(this);

        //-----------Spinner-----------------------
        spinner = v.findViewById(R.id.mace_selector_spinner);
        spinner.setOnItemSelectedListener(this);

        //Data for the Spinner
        List<String> values = new ArrayList<>();

        // Create an ArrayAdapter using the string and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, values);


        //Get decks from Data Base
        mDeckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);
        mDeckViewModel.getAllDecks().observe(getViewLifecycleOwner(), decks -> {
            if (decks.isEmpty()) {
                adapter.add(getString(R.string.NoDeck));
            } else {
                for (Deck s : decks) {
                    adapter.add(s.getNameText());
                }
            }
        });

        //set last selected item
        spinnerValue = adapter.getPosition(savedDeck);
        spinner.setSelection(spinnerValue);


        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        return v;
    }


    //---------Manejador de eventos del dropdown
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        deckName = adapterView.getItemAtPosition(i).toString();
        //store selected item
        savedDeck = spinner.getSelectedItem().toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), ReviewCardsActivity.class);
        intent.putExtra("message_key", deckName);
        startActivity(intent);
    }
}