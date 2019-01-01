package com.bashalir.go4lunch.Utils;

import com.bashalir.go4lunch.Models.GPlaces.Close;
import com.bashalir.go4lunch.Models.GPlaces.Open;
import com.bashalir.go4lunch.Models.GPlaces.OpeningHours;
import com.bashalir.go4lunch.Models.GPlaces.Period;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class UtilitiesTest {

    @Test
    public void shouldReturnTrueIf2CalendarisReturn() {
        List<Calendar> testOpenningHour=new Utilities().DateFormatterWeekdayTextGmap("Sunday: 12:00 – 11:30 PM");
        assertTrue(testOpenningHour.size()==2);
    }

    @Test
    public void shouldReturn(){
        Period period = new Period();

        period.getOpen().setDay(6);
        period.getOpen().setTime("2000");


    }

    @Test
    public void shouldReturnClosingSoon(){
        assertEquals("Closing soon",new Utilities().getOpenUntil(false,"2000"));

    }

    @Test
    public void shouldReturnOpenUntil11pm(){
        assertEquals("Open until 11.30pm",new Utilities().getOpenUntil(false,"2330"));

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
    public void shouldReturnFalse(){
        assertEquals("Open until 11.30pm",new Utilities().getOpenUntil(false,"2330"));

    }


    @Test
    public void getRestaurantHours() {

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

        open1.setDay(0);
        open1.setTime("0800");
        close1.setDay(1);
        close1.setTime("0200");

        period1.setOpen(open1);
        period1.setClose(close1);

        open2.setDay(2);
        close2.setDay(2);
        open2.setTime("0800");
        close2.setTime("1200");
        period2.setOpen(open2);
        period2.setClose(close2);

        open3.setDay(2);
        close3.setDay(3);
        open3.setTime("1400");
        close3.setTime("0200");
        period3.setOpen(open3);
        period3.setClose(close3);


        periods = Arrays.asList(period1, period2, period3);

        assertEquals(8,new Utilities().extractHoursOfThisDay(periods).get(1).get(Calendar.HOUR));

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