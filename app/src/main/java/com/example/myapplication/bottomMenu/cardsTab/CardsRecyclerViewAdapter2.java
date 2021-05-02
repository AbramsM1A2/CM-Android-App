package com.example.myapplication.bottomMenu.cardsTab;

import android.content.Intent;
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
    private OnCardListener mOnCardListener;

    public CardsRecyclerViewAdapter2(List<Card> items, OnCardListener onCardListener) {
        mCardList = items;
        this.mOnCardListener = onCardListener;
    }

    @Override
    //Metodo que hace el inflate de la view con su viewholder
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_card, parent, false);
        return new ViewHolder(view, mOnCardListener);
    }

    @Override
    //Metodo que bindea los datos en una view
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mCardList.get(position);
        holder.mFrontView.setText(mCardList.get(position).getFrontText());
        holder.mBackView.setText(mCardList.get(position).getBackText());
        holder.mId = mCardList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return mCardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView mFrontView;
        public final TextView mBackView;
        public Integer mId;
        public Card mItem;
        OnCardListener onCardListener;

        public ViewHolder(View view, OnCardListener onCardListener) {
            super(view);
            mView = view;
            mFrontView = (TextView) view.findViewById(R.id.front_content);
            mBackView = (TextView) view.findViewById(R.id.back_content);
            this.onCardListener = onCardListener;

            view.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            //TODO QUITAR ESTOS PRINTS Y LLAMAR A LA CREACION DE UNA ACTIVITY QUE MODIFIQUE Y PERMITA AL USUARIO ELIMINAR LA CARTA
            onCardListener.onCardClick(getAdapterPosition());

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mFrontView.getText() + "'";
        }


    }
        public interface OnCardListener{
            void onCardClick(int position);
        }
}