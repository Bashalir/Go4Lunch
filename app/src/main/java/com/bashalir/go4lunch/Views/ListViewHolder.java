package com.bashalir.go4lunch.Views;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bashalir.go4lunch.Models.Restaurant;
import com.bashalir.go4lunch.R;
import com.bashalir.go4lunch.Utils.Utilities;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;
import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;
import static com.bumptech.glide.request.RequestOptions.option;
import static com.bumptech.glide.request.RequestOptions.overrideOf;


public class ListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_list_view_item_name_tv)
    TextView mName;
    @BindView(R.id.fragment_list_view_item_address_tv)
    TextView mAddress;
   @BindView(R.id.fragment_list_view_item_open_tv)
    TextView mOpen;
    @BindView(R.id.fragment_list_view_item_photo_iv)
    ImageView mPhoto;
    @BindView(R.id.fragment_list_view_item_rating_rb)
    RatingBar mStar;

    View mView;

    public ListViewHolder(View itemView) {
        super(itemView);
        mView=itemView;
        ButterKnife.bind(this, itemView);

    }

    public void updateWithRestaurant(Restaurant restaurant){

        float rating=new Float(restaurant.getStar());


        if (!(restaurant.getOpeningHours()==null))
        {

                String open = new Utilities().getOpen(restaurant.getOpeningHours().getPeriods());
                mOpen.setText(open);
                mOpen.setTextColor(Color.parseColor("#04CD6C"));

                if (open.equals("Closing soon") || open.equals("Closed")) {
                    mOpen.setTextColor(Color.parseColor("#e30425"));
                }




        }

        mName.setText(restaurant.getName());
        mAddress.setText(restaurant.getAddress());
        mStar.setRating(rating);

        Glide
                .with(mView)
                .load(restaurant.getLinkPhoto())
                .apply(overrideOf(256,256))
                .apply(centerCropTransform())
                .into(mPhoto);
    }
}
