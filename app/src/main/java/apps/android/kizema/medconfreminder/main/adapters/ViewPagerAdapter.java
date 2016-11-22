package apps.android.kizema.medconfreminder.main.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import java.util.List;

/**
 * Created by A.Kizema on 22.11.2016.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<DataHolder> dataHolders;

    private Context context;

    private SparseArray<Fragment> fragments = new SparseArray<>();

    public static class DataHolder{
        public String fragment;
        public Bundle bundle;
        public String title;

        public DataHolder(String fragment, Bundle bundle, String title){
            this.fragment = fragment;
            this.bundle = bundle;
            this.title = title;
        }
    }

    public ViewPagerAdapter(FragmentManager fragmentManager, Context context,
                            List<DataHolder> fragments) {
        super(fragmentManager);
        this.dataHolders = fragments;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (dataHolders == null){
            return 0;
        }

        return dataHolders.size();
    }

    public Fragment getFragment(int pos){
        return fragments.get(pos);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = Fragment.instantiate(context, dataHolders.get(position).fragment, dataHolders.get(position).bundle);
        fragments.put(position, f);
        return f;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return dataHolders.get(position).title;
    }

}
