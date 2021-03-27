package com.example.myapplication.bottomMenu.cardsTab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;


public class CardRecyclerViewAdapter2 extends RecyclerView.Adapter<CardRecyclerViewAdapter2.ViewHolder> {

    private final List<CardItems> mCardItemsList;

    public CardRecyclerViewAdapter2(List<CardItems> items) {
        mCardItemsList = items;
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
        holder.mItem = mCardItemsList.get(position);
        holder.mFrontView.setText(mCardItemsList.get(position).getFront());
        holder.mBackView.setText(mCardItemsList.get(position).getBack());
    }

    @Override
    public int getItemCount() {
        return mCardItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mFrontView;
        public final TextView mBackView;
        public CardItems mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mFrontView = (TextView) view.findViewById(R.id.front_content);
            mBackView = (TextView) view.findViewById(R.id.back_content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mFrontView.getText() + "'";
        }
    }
}