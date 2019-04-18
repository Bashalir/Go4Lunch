package com.bashalir.go4lunch.Views.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bashalir.go4lunch.Models.Restaurant;
import com.bashalir.go4lunch.R;
import com.bashalir.go4lunch.Views.ListViewHolder;

import java.util.List;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewHolder> {

    // CONSTRUCTOR
    private List<Restaurant> mRestaurant;

    // CONSTRUCTOR
    public ListViewAdapter(List<Restaurant> mRestaurant) {
        this.mRestaurant = mRestaurant;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_list_view_item, parent, false);

        return new ListViewHolder(view);
        }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.updateWithRestaurant(mRestaurant.get(position));

    }

    @Override
    public int getItemCount() {
        return mRestaurant.size();
    }

    public List<Restaurant> getRestaurant() {return mRestaurant;}


}
