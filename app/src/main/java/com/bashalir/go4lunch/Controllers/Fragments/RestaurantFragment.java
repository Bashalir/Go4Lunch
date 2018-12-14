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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

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

        Bundle arguments=this.getArguments();
        ArrayList getArgument = arguments.getCharSequenceArrayList("KEY2");
        Text.setText((CharSequence) getArgument.get(1));

        /*
        mListRestaurant= new ListRestaurant();

        for (int i=0;i<=getArgument.size()-1;i++)
        {
            String idPlace= (String) getArgument.get(i);
            requestRestaurantDetails(idPlace);
        }

        mListRestaurant.setRestaurant(mRestaurant);
*/
        String url="https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key="+BuildConfig.GOOGLE_MAPS_API_KEY;

        Glide.with(this)
                .load(url)
                .into(testImage);

        return view;
    }

    private void requestRestaurantDetails(String idPlace) {

        Log.d(mTag, idPlace+"");
        mDisp=GMapStream.streamFetchDetailsRestaurant(idPlace,"en").subscribeWith(new DisposableObserver<GPlaces>(){

            @Override
            public void onNext(GPlaces gPlaces) {
                Log.d(mTag, "NEXT ");

                if (gPlaces.getStatus().equals("OK")) {

                    createListRestaurant(gPlaces,idPlace);
                }
                else{
                    Toast.makeText(mContext, R.string.NoRestaurant, Toast.LENGTH_SHORT).show();
                }

            }

            private void createListRestaurant(GPlaces gPlaces, String idPlace) {

                Restaurant restaurant=new Restaurant();



                restaurant.setName(gPlaces.getResult().getName());
                restaurant.setAddress(gPlaces.getResult().getVicinity());
                restaurant.setStar(gPlaces.getResult().getRating());

                mRestaurant.add(restaurant);

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
}
