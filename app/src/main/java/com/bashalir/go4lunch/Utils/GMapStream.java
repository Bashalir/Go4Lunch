package com.bashalir.go4lunch.Utils;

import com.bashalir.go4lunch.Models.GMap.GMap;
import com.bashalir.go4lunch.Models.ListRestaurant;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GMapStream {


    public static Observable<GMap> streamFetchListRestaurant(String gMapLocation) {
        GMapService gMapService = GMapService.retrofit.create(GMapService.class);
        String location;
        return gMapService.getListRestaurant(gMapLocation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
}
