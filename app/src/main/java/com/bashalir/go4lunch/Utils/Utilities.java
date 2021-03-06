package com.bashalir.go4lunch.Utils;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfRenderer;
import android.location.LocationManager;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Pair;

import com.bashalir.go4lunch.Controllers.Activities.PageActivity;
import com.bashalir.go4lunch.Models.GMap.Location;
import com.bashalir.go4lunch.Models.GPlaces.OpeningHours;
import com.bashalir.go4lunch.Models.GPlaces.Period;
import com.bashalir.go4lunch.Models.ListRestaurant;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.getInstance;

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

    public List<Calendar> DateFormatterWeekdayTextGmap(String dayText) {

        Calendar calendarOpen = Calendar.getInstance();
        Calendar calendarClose = Calendar.getInstance();

        List<Calendar> OpenningHour = new ArrayList<>();

        dayText = dayText.replaceAll("[^0-9_:_–]", "");

        String[] dayTextSplit = dayText.split("–");
        String[] dayOfWeekSplitOpen = dayTextSplit[0].split(":");
        String[] dayOfWeekSplitClose = dayTextSplit[1].split(":");

        int openHour = Integer.parseInt(dayOfWeekSplitOpen[1]);
        int openMinute = Integer.parseInt(dayOfWeekSplitOpen[2]);

        int closeHour = Integer.parseInt(dayOfWeekSplitClose[0]);
        int closeMinute = Integer.parseInt(dayOfWeekSplitClose[1]);

        calendarOpen.set(Calendar.HOUR, openHour);
        calendarOpen.set(Calendar.MINUTE, openMinute);

        calendarClose.set(Calendar.HOUR, closeHour);
        calendarClose.set(Calendar.MINUTE, closeMinute);

        OpenningHour.add(calendarOpen);
        OpenningHour.add(calendarClose);

        return OpenningHour;
    }



    public boolean getOpenMidnight(int dayOpen, int dayClose) {


        if (dayOpen==dayClose) {return false;}
        return true;
    }


    public String getOpenUntil(boolean night,String timeClose){

        Calendar closureHour = Calendar.getInstance();
        Calendar now =Calendar.getInstance();

        int dayOfWeek=now.get(DAY_OF_WEEK);

        int hourClose= Integer.parseInt(timeClose.substring(0,2));
        int minuteClose= Integer.parseInt(timeClose.substring(2,4));

        String pattern="h.mm";
        if (minuteClose==0){pattern="h";}

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        closureHour.set(Calendar.HOUR_OF_DAY,hourClose);
        closureHour.set(Calendar.MINUTE,minuteClose);

        if (night==true)
        {closureHour.add(Calendar.DAY_OF_MONTH,1);}


        String showTime=sdf.format(closureHour.getTime());

        if (hourClose>12) {showTime=showTime+"pm";} else {showTime=showTime+"am";}

       long diff=closureHour.getTimeInMillis()-now.getTimeInMillis();
       if (diff<3600000 && diff>0) {return "Closing soon";}

      //  if (diff<0) {return "Closed";}

        return "Open until "+showTime;
    }

    public Boolean closingSoon(Calendar c){

        long diff=c.getTimeInMillis()-now().getTimeInMillis();
        if (diff<1800000) {return true;}

        return false;
    }





    public String formatDateToHour(Calendar c) {

        String pattern="h.mm";
        if (c.get(Calendar.MINUTE)==0){pattern="h";}

        SimpleDateFormat sdf = new SimpleDateFormat(pattern,Locale.getDefault());
        String formatHour=sdf.format(c.getTime());

        if (c.get(Calendar.HOUR_OF_DAY)>12) {formatHour=formatHour+"pm";} else {formatHour=formatHour+"am";}

        return formatHour;
    }

    public Double ratingThreeStar(double rating){

        return rating=(rating/5)*3;

    }


    public String openUntil ( List<Calendar>  restaurantHours)
    {
        Calendar now = Calendar.getInstance();

        int i=0;

        if (restaurantHours.isEmpty()){
            return "Open 24/7";
        }

        if (restaurantHours.size()>2){
            if (restaurantHours.get(1).before(now)) {i=2;}
        }

        if (restaurantHours.get(i).before(now) && restaurantHours.get(i+1).after(now) ) {
                if (closingSoon(restaurantHours.get(i+1))) {
                    return "Closing soon";
                }

                return "Open until " + formatDateToHour(restaurantHours.get(i+1));
            }

        return "Closed";
    }

    public Calendar now(){
        Calendar now = Calendar.getInstance();
        return now;
    }

    public String getOpen(List<Period> periods)
    {
        return openUntil(extractHoursOfThisDay(periods));
    }




    public List<Calendar> extractHoursOfThisDay(List<Period> periods){

        List<Calendar> restaurantHours = new ArrayList<>();
        List periodDayHour= new ArrayList<>();

        Calendar calendarClose;

        //day a number from 0–6, corresponding to the days of the week, starting on Sunday.
        int thisDay=now().get(DAY_OF_WEEK)-1;


       for (int i=0;i<=periods.size()-1;i++)
        {
            int scanDay=periods.get(i).getOpen().getDay();

            if (periods.get(i).getOpen().getTime().equals("0000") && periods.get(i).getOpen().getDay()==0)
            {return restaurantHours; }

            //Retrieve the closing hours of the day
            if (scanDay==thisDay) {

                String openTime=periods.get(i).getOpen().getTime();
                String closeTime=periods.get(i).getClose().getTime();

                restaurantHours.add(StringHourMinuteToCalendarNow(openTime));
                calendarClose=StringHourMinuteToCalendarNow(closeTime);

                if (periods.get(i).getClose().getDay()>scanDay) {

                    calendarClose.add(Calendar.DAY_OF_MONTH,1);
                }

                restaurantHours.add(calendarClose);

            }
        }

        return restaurantHours;
    }


    public Calendar StringHourMinuteToCalendarNow(String hour)
    {
        Calendar calendar= Calendar.getInstance();

        int hourOpen= Integer.parseInt(hour.substring(0,2));
        int minuteOpen= Integer.parseInt(hour.substring(2,4));

        calendar.set(Calendar.HOUR_OF_DAY,hourOpen);
        calendar.set(Calendar.MINUTE,minuteOpen);

        return calendar;
    }


}
