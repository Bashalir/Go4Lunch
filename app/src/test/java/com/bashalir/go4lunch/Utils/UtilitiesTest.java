package com.bashalir.go4lunch.Utils;

import android.content.ServiceConnection;

import com.bashalir.go4lunch.Models.GPlaces.Close;
import com.bashalir.go4lunch.Models.GPlaces.Open;
import com.bashalir.go4lunch.Models.GPlaces.OpeningHours;
import com.bashalir.go4lunch.Models.GPlaces.Period;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Utilities.class)

public class UtilitiesTest {




    @Test
    public void shouldReturnTrueIf2CalendarisReturn() {
        List<Calendar> testOpenningHour=new Utilities().DateFormatterWeekdayTextGmap("Sunday: 12:00 – 11:30 PM");
        assertTrue(testOpenningHour.size()==2);
    }



   @Test
    public void shouldReturnClosingSoon(){
        Calendar endofDays =Calendar.getInstance();
        endofDays.set(Calendar.HOUR_OF_DAY,20);
        endofDays.set(Calendar.MINUTE,0);

        Calendar timeclose =Calendar.getInstance();
        timeclose.set(Calendar.HOUR_OF_DAY,19);
        timeclose.set(Calendar.MINUTE,10);


        PowerMockito.mockStatic(Calendar.class);
        Mockito.when(Calendar.getInstance()).thenReturn(endofDays,timeclose);
        Utilities testClass= new Utilities();

        assertEquals("Closing soon",new Utilities().getOpenUntil(false,"2000"));

    }


    @Test
    public void shouldReturnOpenUntil11pm(){
        Calendar endofDays =Calendar.getInstance();
        endofDays.set(Calendar.HOUR_OF_DAY,22);
        endofDays.set(Calendar.MINUTE,45);

        Calendar timeclose =Calendar.getInstance();
        timeclose.set(Calendar.HOUR_OF_DAY,12);
        timeclose.set(Calendar.MINUTE,00);


        PowerMockito.mockStatic(Calendar.class);
        Mockito.when(Calendar.getInstance()).thenReturn(endofDays,timeclose);
        Utilities testClass= new Utilities();

        assertEquals("Open until 11.30pm",new Utilities().getOpenUntil(false,"2245"));

    }

    @Test
    public void shouldReturngetOpenNightTrue(){
       assertEquals(true,new Utilities().getOpenMidnight(4,5));

    }

    @Test
    public void shouldReturngetOpenNightFalse(){
        assertEquals(false,new Utilities().getOpenMidnight(4,4));

    }



    @Test
    public void getRestaurantHours() {

        Calendar calendar= Calendar.getInstance();

        List<Period> periods;

        Period period1=new Period();
        Period period2=new Period();
        Period period3=new Period();

        Open open1=new Open();
        Close close1=new Close();

        Open open2=new Open();
        Close close2=new Close();

        Open open3=new Open();
        Close close3=new Close();

        open1.setDay((calendar.get(Calendar.DAY_OF_WEEK)));
        open1.setTime("0800");
        close1.setDay(1);
        close1.setTime("0200");

        period1.setOpen(open1);
        period1.setClose(close1);

        open2.setDay((calendar.get(Calendar.DAY_OF_WEEK))-1);
        close2.setDay((calendar.get(Calendar.DAY_OF_WEEK))-1);
        open2.setTime("1200");
        close2.setTime("1500");
        period2.setOpen(open2);
        period2.setClose(close2);

        open3.setDay((calendar.get(Calendar.DAY_OF_WEEK))-1);
        close3.setDay((calendar.get(Calendar.DAY_OF_WEEK))-1);
        open3.setTime("1800");
        close3.setTime("2200");
        period3.setOpen(open3);
        period3.setClose(close3);



        periods = Arrays.asList(period1, period2, period3);

        assertEquals(22,new Utilities().extractHoursOfThisDay(periods).get(3).get(Calendar.HOUR_OF_DAY));


    }

    @Test
    public void closingSoon() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE,15);
        assertEquals(true,new Utilities().closingSoon(now));
        now.add(Calendar.HOUR_OF_DAY,1);
        assertEquals(false,new Utilities().closingSoon(now));
    }

    @Test
    public void formatDateToHour() {

     Calendar c=Calendar.getInstance();
     c.set(Calendar.HOUR_OF_DAY,8);
     c.set(Calendar.MINUTE,0);

     assertEquals("8am",new Utilities().formatDateToHour(c));

     c.set(Calendar.HOUR_OF_DAY,15);
     c.set(Calendar.MINUTE,30);
     assertEquals("3.30pm",new Utilities().formatDateToHour(c));

    }

    @Test
    public void formatDateToHour2() {

        Calendar c=Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY,15);
        c.set(Calendar.MINUTE,30);
        assertEquals("3.30pm",new Utilities().formatDateToHour(c));

    }

    @Test
    public void openUntilOneHour() {

        Calendar fakeNow =Calendar.getInstance();
        fakeNow.set(Calendar.HOUR_OF_DAY,15);
        fakeNow.set(Calendar.MINUTE,0);

        List<Calendar> restaurantHours = new ArrayList<>();

        Calendar open1=Calendar.getInstance();
        Calendar close1=Calendar.getInstance();

        open1.set(Calendar.HOUR_OF_DAY,11);
        close1.set(Calendar.HOUR_OF_DAY,18);
        close1.set(Calendar.MINUTE,0);
        restaurantHours= Arrays.asList(open1,close1);

        PowerMockito.mockStatic(Calendar.class);
        Mockito.when(Calendar.getInstance()).thenReturn(fakeNow);

        assertEquals("Open until 6pm",new Utilities().openUntil(restaurantHours));

    }

    @Test
    public void openUntilTwoHour() {

        List<Calendar> restaurantHours = new ArrayList<>();

        Calendar fakeNow =Calendar.getInstance();
        fakeNow.set(Calendar.HOUR_OF_DAY,14);
        fakeNow.set(Calendar.MINUTE,0);


        Calendar open1=Calendar.getInstance();
        Calendar close1=Calendar.getInstance();
        Calendar open2=Calendar.getInstance();
        Calendar close2=Calendar.getInstance();


        open1.set(Calendar.HOUR_OF_DAY,8);
        close1.set(Calendar.HOUR_OF_DAY,12);
        close1.set(Calendar.MINUTE,0);

        open2.set(Calendar.HOUR_OF_DAY,14);
        open2.set(Calendar.MINUTE,0);
        close2.add(Calendar.DAY_OF_MONTH,0);
        close2.set(Calendar.HOUR_OF_DAY,23);
        close2.set(Calendar.MINUTE,00);

        restaurantHours= Arrays.asList(open1,close1,open2,close2);

        PowerMockito.mockStatic(Calendar.class);
        Mockito.when(Calendar.getInstance()).thenReturn(fakeNow);

        assertEquals("Open until 6pm",new Utilities().openUntil(restaurantHours));
    }

    @Test
    public void shouldReturn5() {
        assertEquals(5,new Utilities().StringHourMinuteToCalendarNow("0500").get(Calendar.HOUR));
    }

    @Test
    public void debugRetrofit() {
    }

    @Test
    public void getOpening() {
    }
}