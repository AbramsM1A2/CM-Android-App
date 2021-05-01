package com.example.myapplication.bottomMenu.homeTab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.Deck.Deck;

import java.util.ArrayList;
import java.util.List;


public class HomeCustomAdapter extends RecyclerView.Adapter<HomeCustomAdapter.ViewHolder> {

    private List<Deck> mDataItemList;
    private final onDeckListener deckListener;

    public HomeCustomAdapter(List<Deck> list, onDeckListener deckListener) {
        this.mDataItemList = list;
        this.deckListener=deckListener;
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

        return new ViewHolder(view,deckListener);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull HomeCustomAdapter.ViewHolder holder, int position) {
        holder.mTextView.setText(mDataItemList.get(position).getNameText()); //Se pilla el nombre
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
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView mTextView;
        private final onDeckListener deckListener;

        public ViewHolder(@NonNull View itemView, onDeckListener listener) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.deckName);
            this.deckListener = listener;

            itemView.setOnClickListener(this);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText() + "'";
        }

        @Override
        public void onClick(View view) {
            deckListener.onListClickListener(getAdapterPosition());
        }
    }

    public interface onDeckListener {
        void onListClickListener(int position);
    }

}
