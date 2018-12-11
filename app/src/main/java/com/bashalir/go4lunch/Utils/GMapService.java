package com.bashalir.go4lunch.Utils;

import com.bashalir.go4lunch.BuildConfig;
import com.bashalir.go4lunch.Models.GMap.GMap;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GMapService {

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    @GET("nearbysearch/json?&radius=500&type=restaurant&key=" + BuildConfig.GOOGLE_MAPS_API_KEY)
    Observable<GMap> getListRestaurant(@Query("gMapLocation") String gMapLocation);

}