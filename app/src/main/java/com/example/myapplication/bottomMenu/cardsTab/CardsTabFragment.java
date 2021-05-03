package com.example.myapplication.bottomMenu.cardsTab;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.Card.Card;
import com.example.myapplication.database.Card.CardViewModel;
import com.example.myapplication.database.Deck.Deck;
import com.example.myapplication.database.Deck.DeckViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardsTabFragment extends Fragment implements AdapterView.OnItemSelectedListener, CardsRecyclerViewAdapter2.OnCardListener  {

    private List<Card> cardList;

    private String deckName = "";
    //
    int positionSelectedInSpinner;

    //Spinner con su adapter
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    //RecyclerView
    private RecyclerView recyclerView;

    //Models de la dataBase
    private CardViewModel mCardViewModel;
    private DeckViewModel mDeckViewModel;

    //Diccionario con mazos y sus IDs;
    private Map<String, Integer> mDecksByName;

    //fabs
    private FloatingActionButton mMainAddFab, mAddCardFab, mAddDeckFab, mEditDeckFab;
    private TextView mAddCardText, mAddDeckText, mEditDeckText;
    private boolean isOpen;
    private Animation mRotateOpen, mRotateClose, mFromBottom, mToBottom;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    public CardsTabFragment() {

    }

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
        spinner = (Spinner) view.findViewById(R.id.cards_tab_mace_selector_spinner);
        spinner.setOnItemSelectedListener(this);

        //Data for the Spinner
        List<String> values = new ArrayList<>();

        // Create an ArrayAdapter using the string and a default spinner layout
        adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        //Iniciar el diccionario
        mDecksByName = new HashMap<String, Integer>();


        //DB
        mCardViewModel = new ViewModelProvider(this).get(CardViewModel.class);
        mDeckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);
        mDeckViewModel.getAllDecks().observe(getViewLifecycleOwner(), decks -> {
            values.clear();
            for (Deck s : decks) {
                //Podriamos poner solo una estructura de datos
                mDecksByName.put(s.getNameText(), s.getId());
                adapter.add(s.getNameText());
            }

        });
        cardList = new ArrayList<Card>();

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        //Accedemos al recyclerView contenido en el view del .xml
        recyclerView = (RecyclerView) view.findViewById(R.id.List_of_Cards);
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));



        //FABS

        mMainAddFab = view.findViewById(R.id.floating_action_button);
        mAddCardFab = view.findViewById(R.id.floating_add_card_button);
        mAddDeckFab = view.findViewById(R.id.floating_add_deck_button);
        mEditDeckFab = view.findViewById(R.id.floating_edit_deck_button);

        mAddCardText = view.findViewById(R.id.textView_add_card);
        mAddDeckText = view.findViewById(R.id.textView_add_deck);
        mEditDeckText = view.findViewById(R.id.textView_edit_deck);

        mRotateOpen = AnimationUtils.loadAnimation(context, R.anim.rotate_open_anim);
        mRotateClose = AnimationUtils.loadAnimation(context, R.anim.rotate_close_anim);
        mFromBottom = AnimationUtils.loadAnimation(context, R.anim.from_bottom_anim);
        mToBottom = AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim);

        isOpen = false;

        mMainAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    //Animaciones
                    mMainAddFab.startAnimation(mRotateClose);

                    mAddCardFab.startAnimation(mToBottom);
                    mAddDeckFab.startAnimation(mToBottom);
                    mEditDeckFab.startAnimation(mToBottom);
                    mAddCardText.startAnimation(mToBottom);
                    mAddDeckText.startAnimation(mToBottom);
                    mEditDeckText.startAnimation(mToBottom);

                    //Visibilidad
                    mAddCardFab.setVisibility(View.INVISIBLE);
                    mAddDeckFab.setVisibility(View.INVISIBLE);
                    mEditDeckFab.setVisibility(View.INVISIBLE);
                    mAddCardText.setVisibility(View.INVISIBLE);
                    mAddDeckText.setVisibility(View.INVISIBLE);
                    mEditDeckText.setVisibility(View.INVISIBLE);

                    isOpen = false;
                } else {
                    //Animaciones
                    mMainAddFab.startAnimation(mRotateOpen);
                    mAddCardFab.startAnimation(mFromBottom);
                    mAddDeckFab.startAnimation(mFromBottom);
                    mEditDeckFab.startAnimation(mFromBottom);
                    mAddCardText.startAnimation(mFromBottom);
                    mAddDeckText.startAnimation(mFromBottom);
                    mEditDeckText.startAnimation(mFromBottom);
                    //Visibilidad
                    mAddCardFab.setVisibility(View.VISIBLE);
                    mAddDeckFab.setVisibility(View.VISIBLE);
                    mEditDeckFab.setVisibility(View.VISIBLE);
                    mAddCardText.setVisibility(View.VISIBLE);
                    mAddDeckText.setVisibility(View.VISIBLE);
                    mEditDeckText.setVisibility(View.VISIBLE);

                    isOpen = true;
                }
            }
        });

        mAddCardFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Para saber que mazo estaba seleccionado
                positionSelectedInSpinner = spinner.getSelectedItemPosition();

                //Aqui tengo que hacer que comience la actividad en la que se añade a la base de datos una carta a un mazo elegido
                Intent i = new Intent(getActivity(), AddCardActivity.class);
                //Para que se vuelva a cerrar el fab button
                mMainAddFab.callOnClick();
                startActivity(i);
                spinner.setSelection(0);
            }
        });

        mAddDeckFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Para saber que mazo estaba seleccionado
                //positionSelectedInSpinner = spinner.getSelectedItemPosition();
                //Aqui tengo que hacer que comience la actividad en la que se añade a la base de datos un mazo
                Intent i = new Intent(getActivity(), AddDeckActivity.class);
                //Para que se vuelva a cerrar el fab button
                mMainAddFab.callOnClick();
                startActivity(i);
                spinner.setSelection(0);
            }
        });

        mEditDeckFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Para saber que mazo estaba seleccionado
                //positionSelectedInSpinner = spinner.getSelectedItemPosition();
                //Aqui tengo que hacer que comience la actividad en la que se edita un mazo de la base de datos
                Intent i = new Intent(getActivity(), EditDeckActivity.class);
                //Para que se vuelva a cerrar el fab button
                mMainAddFab.callOnClick();
                startActivity(i);
                spinner.setSelection(0);
            }
        });

        setTheme();
        return view;

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //TODO EL PROBLEMA QUE HAY QUE ARREGLAR ES QUE ONITEMSELECTED NO VUELVE A SER EJECUTADO CUANDO VUELVE
        // DE UNA ACTIVIDAD POR LO TANTO SE GUARDA LA POSICION DEL SPINNER QUE HABIA ANTES.
        // SI POR EJEMPLO CAMBIAMOS EL LIVE DATA A UNA LISTA NO ESTOY SEGURO DE QUE FUNCIONE PERO SEGURAMENTE SI QUE
        // LO HAGA ARREGLANDO LOS LIFECYCLE Y TAL

        deckName = parent.getItemAtPosition(position).toString();
        Integer deckId = mDecksByName.get(deckName);

        mCardViewModel.getAllCardsWithThisId(deckId).observe(getViewLifecycleOwner(), cards -> {
            //SE PUEDE HACER UN CLEAR O CREAR UNA NUEVA LISTA, MIRAR CUAL SERIA LA MEJOR OPCION
            cardList.clear();
            for (Card c : cards) {
                cardList.add(c);
            }
            //Intento de arreglo fallido
            //positionSelectedInSpinner = spinner.getSelectedItemPosition();
            //spinner.setSelection(positionSelectedInSpinner);
            recyclerView.setAdapter(new CardsRecyclerViewAdapter2(cardList,this));
        });

        // POR PREDETERMINADO, SE VA A HACER CLICK SIEMPRE EN ESTE ONITEMSELECTED CADA VEZ QUE SE ABRA LA PESTAÑA CARDS

        //Toast.makeText(parent.getContext(), "The deck id is " + deckId, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCardClick(int position) {
        //Para saber que mazo estaba seleccionado
        positionSelectedInSpinner = adapter.getPosition(deckName);
        Intent intent  = new Intent(getActivity(),EditCardActivity.class);
        intent.putExtra("selected_card",cardList.get(position));
        startActivity(intent);
        spinner.setSelection(positionSelectedInSpinner);

    }
    private void setTheme() {

        int nightModeFlags = this.getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
            //claro
            mMainAddFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_blue_700)));
            mAddCardFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cp_light_blue_700)));
            mAddDeckFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cp_light_blue_700)));
            mEditDeckFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cp_light_blue_700)));
            mAddCardText.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cp_light_blue_700)));
            mAddCardText.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            mAddDeckText.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cp_light_blue_700)));
            mAddDeckText.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            mEditDeckText.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cp_light_blue_700)));
            mEditDeckText.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));

        } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            //oscuro
            mMainAddFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_blue_200)));
            mAddCardFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cp_light_blue_200)));
            mAddDeckFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cp_light_blue_200)));
            mEditDeckFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cp_light_blue_200)));
            mAddCardText.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cp_light_blue_200)));
            mAddCardText.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            mAddDeckText.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cp_light_blue_200)));
            mAddDeckText.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            mEditDeckText.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cp_light_blue_200)));
            mEditDeckText.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));

        }
    }
}