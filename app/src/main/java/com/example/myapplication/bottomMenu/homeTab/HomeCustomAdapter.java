package com.example.myapplication.bottomMenu.homeTab;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ReviewCardsActivity;
import com.example.myapplication.database.Deck.Deck;

import java.util.ArrayList;
import java.util.List;


public class HomeCustomAdapter extends RecyclerView.Adapter<HomeCustomAdapter.ViewHolder> {

    private List<Deck> mDataItemList;
    private final HomeFragment.onFragmentInteraction mListener;

    public HomeCustomAdapter(HomeFragment.onFragmentInteraction listener) {
        mListener = listener;
    }

    public void setListData(List<Deck> dataItemList) {
        //setup new list
        if (mDataItemList == null) {
            mDataItemList = new ArrayList<>();
        }
        mDataItemList.clear();
        mDataItemList.addAll(dataItemList);
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public HomeCustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.deck_item, parent, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull HomeCustomAdapter.ViewHolder holder, int position) {

        holder.mItem = mDataItemList.get(position);
        holder.mTextView.setText(mDataItemList.get(position).getNameText()); //Se pilla el nombre

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListClickListener(holder.mItem);

                }
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager
    @Override
    public int getItemCount() {
        if (mDataItemList != null) {
            return mDataItemList.size();
        } else {
            return 0;
        }
    }


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTextView;
        private final View mView;
        private Deck mItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mTextView = itemView.findViewById(R.id.deckName);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText() + "'";
        }
    }

}
