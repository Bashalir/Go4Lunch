package com.bashalir.go4lunch.Utils;


import android.util.Pair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<Calendar> DateFormatterWeekdayTextGmap(String dayText)  {

        Calendar calendarOpen =Calendar.getInstance();
        Calendar calendarClose =Calendar.getInstance();

        List<Calendar> OpenningHour = new ArrayList<>();

      dayText=dayText.replaceAll("[^0-9_:_–]","");

      String [] dayTextSplit= dayText.split("–");
      String [] dayOfWeekSplitOpen=dayTextSplit[0].split(":");
      String [] dayOfWeekSplitClose=dayTextSplit[1].split(":");

      int openHour= Integer.parseInt(dayOfWeekSplitOpen[1]);
      int openMinute= Integer.parseInt(dayOfWeekSplitOpen[2]);

      int closeHour= Integer.parseInt(dayOfWeekSplitClose[0]);
      int closeMinute= Integer.parseInt(dayOfWeekSplitClose[1]);

      calendarOpen.set(Calendar.HOUR,openHour);
      calendarOpen.set(Calendar.MINUTE,openMinute);

      calendarClose.set(Calendar.HOUR,closeHour);
      calendarClose.set(Calendar.MINUTE,closeMinute);

      OpenningHour.add(calendarOpen);
      OpenningHour.add(calendarClose);

      return OpenningHour;
    }

    public String getOpening() {


        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);

        return  String.valueOf(minute);
    }


}
