package com.bashalir.go4lunch.Controllers.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bashalir.go4lunch.Controllers.Activities.RestaurantActivity;
import com.bashalir.go4lunch.Models.GMap.GMap;
import com.bashalir.go4lunch.Models.ListMarkerGmap;
import com.bashalir.go4lunch.Models.MarkerGmap;
import com.bashalir.go4lunch.R;
import com.bashalir.go4lunch.Utils.GMapStream;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;


import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback {

    @BindView(R.id.map)
    MapView mMapView;

    private ArrayList<CharSequence> mListIdPlace;

    private ListMarkerGmap mListMarkerGmap;

    private final String mTag = getClass().getSimpleName();

    private Context mContext;
    private SupportMapFragment mMapFragment;
    private final LatLng mDefaultLocation = new LatLng(48.858093, 2.294694);
    private static final int DEFAULT_ZOOM = 15;

    private Disposable mDisp;
    private GoogleMap mMap;

    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;

    MapFragmentListener mListener;
    ListMarkerGmap mListRestaurant;

    public ListMarkerGmap getListRestaurant() {
        return mListRestaurant;
    }

    public void setListRestaurant(ListMarkerGmap mListRestaurant) {
        this.mListRestaurant = mListRestaurant;
    }

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance(int position) {
        return (new MapFragment());
    }



    public interface MapFragmentListener{
        public void mapListRestaurant(ArrayList<CharSequence> listRestaurant );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (MapFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }

public void onDestroy() {
    super.onDestroy();
    this.disposeWhenDestroy();
}

    private void disposeWhenDestroy() {
    if (mDisp != null && !mDisp.isDisposed())
        mDisp.dispose();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

    mContext=getContext();
        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(mContext);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(getContext());

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());



        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mMapFragment=(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mMapFragment == null) {

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            mMapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mMapFragment).commit();
        }


        mMapFragment.getMapAsync(this);


        return view;
    }


    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        showCurrentPlace();




        mMap.setOnMarkerClickListener(this);

    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */

        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {

                        if (task.isSuccessful()) {

                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                            requestRestaurantList(mLastKnownLocation.getLatitude()+","+mLastKnownLocation.getLongitude());

                        } else {
                            Log.d("TAG", "Current location is null. Using defaults.");
                            Log.e("TAG", "Exception: %s", task.getException());

                            requestRestaurantList(mDefaultLocation.latitude+","+mDefaultLocation.longitude);

                            mMap.animateCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }


                });


            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }


    }


    private void requestRestaurantList(String gmapLocation) {

     Log.d(mTag, gmapLocation+"");
        mDisp=GMapStream.streamFetchListRestaurant(gmapLocation).subscribeWith(new DisposableObserver<GMap>(){


            @Override
            public void onNext(GMap gMap) {
                Log.d(mTag, "NEXT ");

                if (gMap.getStatus().equals("OK")) {

                    createListMarker(gMap);

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


    private void createListMarker(GMap gMap) {
        mListMarkerGmap = new ListMarkerGmap();
        ArrayList<MarkerGmap> listMarkerGmap = new ArrayList<>();

        mListMarkerGmap.setSize(gMap.getResults().size());

        ArrayList<CharSequence>listIdPlace=new ArrayList<>();

        for (int i = 0; i <= mListMarkerGmap.getSize() - 1; i++) {
            MarkerGmap markergmap=new MarkerGmap();
            markergmap.setPosition(new LatLng(gMap.getResults().get(i).getGeometry().getLocation().getLat(),gMap.getResults().get(i).getGeometry().getLocation().getLng()));
            markergmap.setIdGmap(gMap.getResults().get(i).getId());
            markergmap.setIdPlace(gMap.getResults().get(i).getPlaceId());
            listMarkerGmap.add(markergmap);

            listIdPlace.add(gMap.getResults().get(i).getPlaceId());

            addGmapMarker(markergmap);

        }
        mListMarkerGmap.setMarkerGmap(listMarkerGmap);
        mListIdPlace=listIdPlace;
        mListener.mapListRestaurant(listIdPlace);
    }

    private void addGmapMarker(MarkerGmap markerGmap) {

        Marker marker;

        marker = mMap.addMarker(new MarkerOptions()
                .position(markerGmap.getPosition())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurant_off))
        );
        marker.setTag(markerGmap);

    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {

        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private void showCurrentPlace() {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.

            @SuppressWarnings("MissingPermission") final
            Task<PlaceLikelihoodBufferResponse> placeResult =
                    mPlaceDetectionClient.getCurrentPlace(null);
            placeResult.addOnCompleteListener
                    (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();

                                // Set the count, handling cases where less than 5 entries are returned.


                                // Release the place likelihood buffer, to avoid memory leaks.
                                likelyPlaces.release();


                            } else {
                                Log.e("TAG", "Exception: %s", task.getException());
                            }
                        }

                    });

        } else {
            // The user has not granted permission.
            Log.i("TAG", "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                          .position(mDefaultLocation));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        MarkerGmap markerGmap = (MarkerGmap) marker.getTag();

        String idPlace=markerGmap.getIdPlace();

      //  Toast.makeText(mContext, markerGmap.getIdPlace()+"", Toast.LENGTH_SHORT).show();
       // Toast.makeText(mContext, idPlace, Toast.LENGTH_SHORT).show();

        //open a Restaurant activity
        Intent restaurantActivity = new Intent(getActivity(), RestaurantActivity.class);
        restaurantActivity.putExtra("idPlace",idPlace);
        startActivityForResult(restaurantActivity, 0);

        return false;
    }
}
