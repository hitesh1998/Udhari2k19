package com.example.udhary;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class RecordAcccessAdapter extends FragmentPagerAdapter {
    public RecordAcccessAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i)
        {
            case 0:
                BlankFragment udhar=new BlankFragment();
                return udhar;
            case 1:
                Paid paid=new Paid();
                return paid;
            case 2:
                Last total=new Last();
                return total;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Udhar";
            case 1:
                return "Paid";
            case 2:
                return "Total";

            default:
                return null;
        }
    }
}
