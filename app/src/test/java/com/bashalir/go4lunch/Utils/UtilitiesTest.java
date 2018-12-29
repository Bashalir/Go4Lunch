package com.bashalir.go4lunch.Utils;

import org.junit.Test;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class UtilitiesTest {

    @Test
    public void shouldReturn7IfDayIsSunday() {
        List<Calendar> testOpenningHour=new Utilities().DateFormatterWeekdayTextGmap("Sunday: 12:00 – 11:30 PM");

        assertTrue(testOpenningHour.size()==2);

        assertEquals("3", testOpenningHour.get(1).getTime());
    }


    @Test
    public void debugRetrofit() {
    }

    @Test
    public void getOpening() {
    }
}