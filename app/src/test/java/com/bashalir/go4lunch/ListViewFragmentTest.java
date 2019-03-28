package com.bashalir.go4lunch;

import com.bashalir.go4lunch.Controllers.Fragments.ListViewFragment;
import com.bashalir.go4lunch.Utils.Utilities;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;


@RunWith(PowerMockRunner.class)
@PrepareForTest(Utilities.class)
public class ListViewFragmentTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void getDeviceLocationTest()
    {
        assertEquals(59.0,new ListViewFragment().getDeviceLocation().getLatitude());
    }



}
