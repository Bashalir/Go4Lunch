package com.bashalir.go4lunch.Controllers.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bashalir.go4lunch.R;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantFragment extends Fragment {

    private SupportMapFragment mMapFragment;

    @BindView(R.id.fragment_restaurant_tv)
    TextView Text;

    public RestaurantFragment() {
        // Required empty public constructor
    }

    public static RestaurantFragment newInstance(int position) {
        return (new RestaurantFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);

        ButterKnife.bind(this,view);

        Bundle arguments=this.getArguments();
        ArrayList getArgument = arguments.getCharSequenceArrayList("KEY2");
        Text.setText((CharSequence) getArgument.get(1));



        return view;
    }

}
