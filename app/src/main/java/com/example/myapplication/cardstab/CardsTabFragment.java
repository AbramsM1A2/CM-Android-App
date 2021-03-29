package com.example.myapplication.cardstab;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.Deck.Deck;
import com.example.myapplication.database.Deck.DeckViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CardsTabFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private List<CardItems> cardItemsList;
    private String deckName="";
    private DeckViewModel mDeckViewModel;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    public CardsTabFragment() {

    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CardsTabFragment newInstance(int columnCount) {
        CardsTabFragment fragment = new CardsTabFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    //logica de la clase
    //aqui va toda la logica de codigo backend y para inicializar cosas
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    //renderizado de la vista
    //to_do lo que sean elementos visuales
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Tomamos el view del archivo .xml
        View view = inflater.inflate(R.layout.fragment_list_card, container, false);
        //-----------Spinner-----------------------
        Spinner spinner = (Spinner) view.findViewById(R.id.cards_tab_mace_selector_spinner);
        spinner.setOnItemSelectedListener(this);

        //Data for the Spinner
        List<String> values = new ArrayList<>();
        values.add("Seleccionar mazo");

        // Create an ArrayAdapter using the string and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);

        //DB
        mDeckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);
        mDeckViewModel.getAllDecks().observe(getViewLifecycleOwner(), decks -> {
            for (Deck s : decks) {
                adapter.add(s.getNameText());
            }
        });

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //Introduce en la lista que mazo se va a enseñar
        cardItemsList = CardItems.createCardList();

        //Accedemos al recyclerView contenido en el view del .xml
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.List_of_Cards);
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new CardsRecyclerViewAdapter(cardItemsList));

        return view;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        deckName = parent.getItemAtPosition(position).toString();

        Toast.makeText(parent.getContext(), "The deck is " +
                parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}