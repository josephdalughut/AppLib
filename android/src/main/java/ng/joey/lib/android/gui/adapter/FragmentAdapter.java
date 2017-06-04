package ng.joey.lib.android.gui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import ng.joey.lib.java.util.Value;

/**
 * Created by root on 4/9/17.
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList;
    private Context context;

    public static FragmentAdapter newInstance(Context context, FragmentManager fm, Fragment... fragments){
        return new FragmentAdapter(context, fm, fragments);
    }

    public FragmentAdapter(Context context, FragmentManager fm, Fragment... fragments) {
        super(fm);
        this.context = context;
        fragmentList = Value.TO.list(fragments);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return Value.IS.nullValue(fragmentList) ? 0 : fragmentList.size();
    }

    public boolean isEmpty(){
        return getCount() == 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Fragment fragment = getItem(position);
        return fragment instanceof TitledFragment ?  ((TitledFragment)fragment).getTitle(context) : super.getPageTitle(position);
    }

    /**
     * Created by root on 4/6/17.
     */

    public static interface TitledFragment{

        public String getTitle(Context context);

    }
}
