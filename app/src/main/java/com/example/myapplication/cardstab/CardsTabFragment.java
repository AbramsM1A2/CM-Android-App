package com.example.myapplication.cardstab;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class CardsTabFragment extends Fragment {

    List<Card> cardList;

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
        cardList = Card.createCardList();
        //Tomamos el view del archivo .xml
        View view = inflater.inflate(R.layout.recycler_view, container, false);

        //Accedemos al recyclerView contenido en el view del .xml
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerId);
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new CardRecyclerViewAdapter2(cardList));
        return view;
    }

}