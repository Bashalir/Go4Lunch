package com.bashalir.go4lunch.Controllers.Fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bashalir.go4lunch.BuildConfig;
import com.bashalir.go4lunch.Models.GMap.GMap;
import com.bashalir.go4lunch.Models.GPlaces.GPlaces;
import com.bashalir.go4lunch.Models.ListRestaurant;
import com.bashalir.go4lunch.Models.Restaurant;
import com.bashalir.go4lunch.R;
import com.bashalir.go4lunch.Utils.GMapStream;
import com.bashalir.go4lunch.Utils.Utilities;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.SupportMapFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantFragment extends Fragment {

    private final String mTag = getClass().getSimpleName();

    private SupportMapFragment mMapFragment;
    private ListRestaurant mListRestaurant;
    private Disposable mDisp;
    private Context mContext;
    private ArrayList<Restaurant> mRestaurant = new ArrayList<>();

    @BindView(R.id.fragment_restaurant_tv)
    TextView Text;
    @BindView(R.id.testimage)
    ImageView testImage;

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
        String idPlace = null;

        Bundle arguments=this.getArguments();
        ArrayList getArgument = arguments.getCharSequenceArrayList("KEY2");
        Text.setText((CharSequence) getArgument.get(1));


       Observable getIdPlace= Observable.fromIterable(getArgument);
        mDisp= (Disposable) getIdPlace
                .timeout(10, TimeUnit.SECONDS)
                .subscribeWith(makeListRestaurant());


        return view;
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
    private void requestRestaurantDetails( String idPlace) {


        mDisp=GMapStream.streamFetchDetailsRestaurant(idPlace,"en").subscribeWith(new DisposableObserver<GPlaces>(){
        GPlaces gp;



            @Override
            public void onNext(GPlaces gPlaces) {
                Log.d(mTag, "NEXT ");

                if (gPlaces.getStatus().equals("OK")) {
                    createRestaurant(gPlaces);
                }
                else{
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

        Restaurant restaurant=new Restaurant();


       /* if (gPlaces.getResult().getPhotos().get(0).getPhotoReference() != null){
            restaurant.setRefPhoto(gPlaces.getResult().getPhotos().get(0).getPhotoReference());}
*/
//        restaurant.setOpen(gPlaces.getResult().getOpeningHours().getOpenNow());
        restaurant.setName(gPlaces.getResult().getName());
        restaurant.setAddress(gPlaces.getResult().getVicinity());
        restaurant.setStar(gPlaces.getResult().getRating());

       addRestaurant(restaurant);

    }

    private void addRestaurant(Restaurant restaurant) {
        mRestaurant.add(restaurant);
        Log.d(mTag, restaurant.getName()+"");
    }

    private void createListRestaurant(ArrayList<Restaurant> listRestaurant){

        mListRestaurant.setRestaurant(listRestaurant);

        String refPhoto=mRestaurant.get(1).getRefPhoto();
        String url="https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+refPhoto+"&key="+BuildConfig.GOOGLE_MAPS_API_KEY;

        Glide.with(this)
                .load(url)
                .into(testImage);
    }
}
