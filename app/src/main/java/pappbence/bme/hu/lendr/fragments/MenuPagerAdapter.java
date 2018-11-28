package pappbence.bme.hu.lendr.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Pair;

import java.util.ArrayList;

public class MenuPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Pair<Fragment, String>> fragmentList = new ArrayList<>();

    public void AddFragment(Fragment fragment, String title){
        fragmentList.add(new Pair<Fragment, String>(fragment, title));
    }

    public MenuPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position).first;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentList.get(position).second;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
