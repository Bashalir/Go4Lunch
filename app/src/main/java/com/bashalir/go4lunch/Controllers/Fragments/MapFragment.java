package com.bashalir.go4lunch.Controllers.Fragments;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bashalir.go4lunch.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;
import java.util.zip.Inflater;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    @BindView(R.id.map)
    MapView mMapView;

    private FragmentActivity mContext;
    private SupportMapFragment mMapFragment;
    static final LatLng PARIS = new LatLng(48.858093, 2.294694);

    private GoogleMap mMap;



    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance(int position) {
        return (new MapFragment());
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mMapFragment=(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        // IF MAP == NULL
        if (mMapFragment == null) {


            FragmentManager fm = getFragmentManager();

           FragmentTransaction ft = fm.beginTransaction();

            // IMPLEMENT SUPPORT MAP FRAGMENT
            mMapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mMapFragment).commit();
        }


        mMapFragment.getMapAsync(this);


        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

}
