package ng.joey.lib.android.gui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ng.joey.lib.java.util.Value;

/**
 * Created by root on 4/12/17.
 */

public abstract class BroadcastFragment extends Fragment {

    private BroadcastReceiver receiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String[] intentActions = getIntentActions();
        if(!Value.IS.emptyValue(intentActions)) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    onIntent(intent);
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            for(String string: intentActions){
                intentFilter.addAction(string);
            }
            getContext().registerReceiver(receiver, intentFilter);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public abstract String[] getIntentActions();
    public abstract void onIntent(Intent intent);

    @Override
    public void onDestroy() {
        if(!Value.IS.nullValue(receiver))
            try{
                getContext().unregisterReceiver(receiver);
            }catch (java.lang.Exception ignored){

            }
        super.onDestroy();
    }
}
