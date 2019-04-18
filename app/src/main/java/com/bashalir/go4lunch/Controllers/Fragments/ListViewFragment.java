package com.bashalir.go4lunch.Controllers.Fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.bashalir.go4lunch.BuildConfig;
import com.bashalir.go4lunch.Controllers.Activities.RestaurantActivity;
import com.bashalir.go4lunch.Models.GPlaces.GPlaces;
import com.bashalir.go4lunch.Models.ListRestaurant;
import com.bashalir.go4lunch.Models.Restaurant;
import com.bashalir.go4lunch.R;
import com.bashalir.go4lunch.Utils.GMapStream;
import com.bashalir.go4lunch.Utils.ItemClickSupport;
import com.bashalir.go4lunch.Utils.Utilities;
import com.bashalir.go4lunch.Views.Adapter.ListViewAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListViewFragment extends Fragment {

    @BindView(R.id.fragment_list_view_rv)
    RecyclerView mRecyclerView;

    private final String mTag = getClass().getSimpleName();

    private SupportMapFragment mMapFragment;
    private ListRestaurant mListRestaurant;
    private Disposable mDisp;
    private ListViewAdapter mAdapter;
    private Context mContext;
    private ArrayList<Restaurant> mRestaurant;
    private String mIdPlace;

    private final LatLng mDefaultLocation = new LatLng(48.8709, 2.3318);
    private Location mMyLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;


    public ListViewFragment() {
        // Required empty public constructor
    }

    public static ListViewFragment newInstance(int position) {
        return (new ListViewFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRestaurant = new ArrayList<>();
        mMyLocation= getDeviceLocation();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, view);


        // Load list of restaurant's idplace
        Bundle arguments = this.getArguments();
        ArrayList getArgument = arguments.getCharSequenceArrayList("KEY2");




        Observable getIdPlace = Observable.fromIterable(getArgument);
        mDisp = (Disposable) getIdPlace
                .timeout(10, TimeUnit.SECONDS)
                .subscribeWith(makeListRestaurant());

        this.configureOnClickRecyclerView(getArgument);

        return view;

    }

    private void configureRecyclerView() {

        mAdapter = new ListViewAdapter(mRestaurant);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    private DisposableObserver<String> makeListRestaurant() {
        return new DisposableObserver<String>() {

            @Override
            public void onNext(String idPlace) {
                Log.d(mTag, "NEXT ");
                requestRestaurantDetails(idPlace);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(mTag, "On Error " + Log.getStackTraceString(e));
            }

            @Override
            public void onComplete() {

                Log.e(mTag, " On Complete !!");
            }
        };
    }

    private void requestRestaurantDetails(String idPlace) {


        mDisp = GMapStream.streamFetchDetailsRestaurant(idPlace, "en").subscribeWith(new DisposableObserver<GPlaces>() {
            GPlaces gp;


            @Override
            public void onNext(GPlaces gPlaces) {
                Log.d(mTag, "NEXT ");

                if (gPlaces.getStatus().equals("OK")) {
                    createRestaurant(gPlaces,idPlace);
                } else {
                    Toast.makeText(mContext, R.string.NoRestaurant, Toast.LENGTH_SHORT).show();
                }

            }


            @Override
            public void onError(Throwable e) {
                Log.e(mTag, "On Error " + Log.getStackTraceString(e));
            }

            @Override
            public void onComplete() {
                configureRecyclerView();
                Log.e(mTag, "On Complete !!" + mRestaurant.size());
            }


        });
    }

    /**
     * Gets the current location of the device
     */
    public Location getDeviceLocation() {

        Location myLocation = new android.location.Location("");

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {

                        if (task.isSuccessful()) {

                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();

                            myLocation.setLatitude(mLastKnownLocation.getLatitude());
                            myLocation.setLongitude(mLastKnownLocation.getLongitude());
                            Log.e(mTag, "Location : " + mLastKnownLocation.getLatitude());


                        }
                    }
                });

        }
        else
        {
            myLocation.setLatitude(mDefaultLocation.latitude);
            myLocation.setLongitude(mDefaultLocation.longitude);

            Log.e(mTag, "Default Location : " + mDefaultLocation.latitude);

        }


        return myLocation;
    }




    public String distanceMeter(Double lat, Double lng, Location myLocation){


       Location restaurantLocation=new android.location.Location("");
        restaurantLocation.setLatitude(lat);
        restaurantLocation.setLongitude(lng);
        Integer distanceInMeters = (int) restaurantLocation.distanceTo(myLocation);


        return distanceInMeters+"m";

    }


    /**
     * Start Restaurant Activity on item clicked
     */
    private void configureOnClickRecyclerView( ArrayList <CharSequence>  getArgument) {


        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_list_view_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        //open a Restaurant activity

                         List<Restaurant> restaurants=mAdapter.getRestaurant();


                        Intent restaurantActivity = new Intent(getActivity(), RestaurantActivity.class);
                        restaurantActivity.putExtra("idPlace", restaurants.get(position).getIdPlace());
                        startActivityForResult(restaurantActivity, 0);

                    }
                });
    }





    public void createRestaurant(GPlaces gPlaces, String idPlace) {

        Restaurant restaurant = new Restaurant();


        if (!gPlaces.getResult().getPhotos().get(0).getPhotoReference().isEmpty()) {
            String refPhoto = gPlaces.getResult().getPhotos().get(0).getPhotoReference();
            String linkPhoto = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + refPhoto + "&key=" + BuildConfig.GOOGLE_MAPS_API_KEY;
            restaurant.setLinkPhoto(linkPhoto);
        }

        if (gPlaces.getResult().getOpeningHours() != null) {
            restaurant.setOpen(gPlaces.getResult().getOpeningHours().getOpenNow());
        }


        restaurant.setIdPlace(idPlace);
        restaurant.setDistance(distanceMeter(gPlaces.getResult().getGeometry().getLocation().getLat(),gPlaces.getResult().getGeometry().getLocation().getLng(),mMyLocation));

        restaurant.setName(gPlaces.getResult().getName());
        restaurant.setAddress(gPlaces.getResult().getVicinity());
        restaurant.setStar(new Utilities().ratingThreeStar(gPlaces.getResult().getRating()));
        restaurant.setOpeningHours(gPlaces.getResult().getOpeningHours());
        mRestaurant.add(restaurant);
        Log.d(mTag, restaurant.getName() + "");

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        this.disposeWhenDestroy();

    }

    private void disposeWhenDestroy() {
        if (mDisp != null && !mDisp.isDisposed()) mDisp.dispose();
    }


}
