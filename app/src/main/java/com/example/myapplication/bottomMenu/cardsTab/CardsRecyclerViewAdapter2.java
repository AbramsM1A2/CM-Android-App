package com.example.myapplication.bottomMenu.cardsTab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.Card.Card;

import java.util.List;


public class CardsRecyclerViewAdapter2 extends RecyclerView.Adapter<CardsRecyclerViewAdapter2.ViewHolder> {

    private final List<Card> mCardList;

    public CardsRecyclerViewAdapter2(List<Card> items) {
        mCardList = items;
    }

    @Override
    //Metodo que hace el inflate de la view con su viewholder
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    //Metodo que bindea los datos en una view
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mCardList.get(position);
        holder.mFrontView.setText(mCardList.get(position).getFrontText());
        holder.mBackView.setText(mCardList.get(position).getBackText());
        holder.mId = mCardList.get(position).getCardId();
    }

    @Override
    public int getItemCount() {
        return mCardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mFrontView;
        public final TextView mBackView;
        public Integer mId;
        public Card mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mFrontView = (TextView) view.findViewById(R.id.front_content);
            mBackView = (TextView) view.findViewById(R.id.back_content);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO QUITAR ESTOS PRINTS Y LLAMAR A LA CREACION DE UNA ACTIVITY QUE MODIFIQUE Y PERMITA AL USUARIO ELIMINAR LA CARTA
                    System.out.println("posicion"+getAdapterPosition());
                    System.out.println("front:"+mFrontView.getText().toString());
                    System.out.println("back:"+mBackView.getText().toString());
                    System.out.println("id:"+mId);
                }
            });

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mFrontView.getText() + "'";
        }
    }
}