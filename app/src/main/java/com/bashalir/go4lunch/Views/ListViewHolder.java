package com.bashalir.go4lunch.Views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bashalir.go4lunch.Models.Restaurant;
import com.bashalir.go4lunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;




public class ListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_list_view_item_name_tv)
    TextView mName;
    @BindView(R.id.fragment_list_view_item_address_tv)
    TextView mAddress;


    public ListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithRestaurant(Restaurant restaurant){

        mName.setText(restaurant.getName());
        mAddress.setText(restaurant.getAddress());


    }
}
