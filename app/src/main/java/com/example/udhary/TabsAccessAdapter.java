package com.example.udhary;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsAccessAdapter extends FragmentPagerAdapter {
    public TabsAccessAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i)
        {
            case 0:
                ContactsFragment contactsFragment=new ContactsFragment();
                return contactsFragment;
            case 1:
                ConectionFragment conectionFragment=new ConectionFragment();
                return conectionFragment;
                default:
                    return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Contacts";
            case 1:
                return "Conections";

            default:
                return null;
        }
    }
}
