package com.bashalir.go4lunch.Views.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bashalir.go4lunch.Controllers.Fragments.ListViewFragment;
import com.bashalir.go4lunch.Controllers.Fragments.MapFragment;
import com.bashalir.go4lunch.Controllers.Fragments.WorkmatesFragment;

public class PageAdapter extends FragmentPagerAdapter {


    public PageAdapter(FragmentManager mgr) {
        super(mgr);
    }

    @Override
    public int getCount() {
        return (3); // 3 - Number of page to show
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MapFragment.newInstance(position);
            case 1:
                return ListViewFragment.newInstance(position);
            case 2:
                return WorkmatesFragment.newInstance(position);
            default:
                return null;

        }

    }
}
