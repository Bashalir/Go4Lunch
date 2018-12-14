package com.bashalir.go4lunch.Utils;


import android.graphics.Bitmap;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class Utilities {


    /**
     * Debug retrofit http request
     *
     * @return
     */
    public okhttp3.OkHttpClient.Builder debugRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors …

        // add logging as last interceptor
        httpClient.addInterceptor(logging);

        return httpClient;
    }


}
