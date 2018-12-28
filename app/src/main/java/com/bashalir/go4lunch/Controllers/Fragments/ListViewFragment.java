package com.bashalir.go4lunch.Controllers.Fragments;


import android.content.Context;
import android.os.Bundle;
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
import com.bashalir.go4lunch.Models.GPlaces.GPlaces;
import com.bashalir.go4lunch.Models.ListRestaurant;
import com.bashalir.go4lunch.Models.Restaurant;
import com.bashalir.go4lunch.R;
import com.bashalir.go4lunch.Utils.GMapStream;
import com.bashalir.go4lunch.Views.Adapter.ListViewAdapter;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
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
    private ArrayList<Restaurant> mRestaurant = new ArrayList<>();


    public ListViewFragment() {
        // Required empty public constructor
    }

    public static ListViewFragment newInstance(int position) {
        return (new ListViewFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, view);

        // Load list of restaurant's idplace
        Bundle arguments = this.getArguments();
        ArrayList getArgument = arguments.getCharSequenceArrayList("KEY2");

        //Load list of details restaurant
        Observable getIdPlace = Observable.fromIterable(getArgument);
        mDisp = (Disposable) getIdPlace
                .timeout(10, TimeUnit.SECONDS)
                .subscribeWith(makeListRestaurant());

        this.configureRecyclerView();

        return view;
    }

    private void configureRecyclerView() {

        mAdapter= new ListViewAdapter(mRestaurant);
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
                Log.e(mTag, "On Complete !!");
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
                    createRestaurant(gPlaces);
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

                Log.e(mTag, "On Complete !!");
            }


        });
    }

    public void createRestaurant(GPlaces gPlaces) {

        Restaurant restaurant = new Restaurant();

        if (!gPlaces.getResult().getPhotos().get(0).getPhotoReference().isEmpty()) {
            String refPhoto = gPlaces.getResult().getPhotos().get(0).getPhotoReference();
            String linkPhoto = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + refPhoto + "&key=" + BuildConfig.GOOGLE_MAPS_API_KEY;
            restaurant.setLinkPhoto(linkPhoto);
        }

        if (gPlaces.getResult().getOpeningHours() != null) {
            restaurant.setOpen(gPlaces.getResult().getOpeningHours().getOpenNow());
        }

        restaurant.setName(gPlaces.getResult().getName());
        restaurant.setAddress(gPlaces.getResult().getVicinity());
        restaurant.setStar(gPlaces.getResult().getRating());

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
