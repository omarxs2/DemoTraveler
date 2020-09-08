package com.app.demotraveler;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class PageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    PageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return  null;
            case 1:
                return null;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}