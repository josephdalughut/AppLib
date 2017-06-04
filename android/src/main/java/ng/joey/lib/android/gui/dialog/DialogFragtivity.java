package ng.joey.lib.android.gui.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import ng.joey.lib.android.R;
import ng.joey.lib.java.util.Value;

/**
 * Created by Joey Dalughut on 8/10/16 at 5:35 PM,
 * Project: Litigy Libraries.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */
public abstract class DialogFragtivity extends DialogFragment {

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView(inflater.inflate(layoutId(), container, false));
        getDialog().setCancelable(cancelable());
        findViews();
        setupViews();
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragtivity.STYLE_NORMAL, R.style.DialogFragtivity);
        bundle(getArguments());
    }

    /**
     * Find a view in this fragments rootView by its id
     * @param resId the resource Id of the view to be found
     * @return a view who's resourceId is {@param resId}
     */
    public View findViewById(int resId){
        return rootView.findViewById(resId);
    }

    private void rootView(View rootView){this.rootView = rootView;}

    /**
     * Get a color by resId
     * @param resId the resource Id for the Color
     * @return
     */
    public Integer getColor(int resId){
        if(Build.VERSION.SDK_INT >= 23) {
            return getRootView().getResources().getColor(resId, null);
        }
        return getRootView().getResources().getColor(resId);
    }

    /**
     * Use this to return the layout for the fragment
     * @return the id for the layout to be inflated as the root view for this fragment
     */
    public abstract int layoutId();

    /**
     * Handle a passed in bundle using this method
     * @param bundle
     */
    public abstract void bundle(Bundle bundle);

    /**
     * Called after the fragment has been created and its view inflated
     */
    public abstract void findViews();

    /**
     * Called after findViews()
     */
    public abstract void setupViews();

    /**
     * Use this to set the width of this dialog fragment
     * @return the layout width for this dialog fragment
     */
    public abstract int layoutWidth();

    /**
     * Use this to set the height of this dialog fragment
     * @return the layout height for this dialog fragment
     */
    public abstract int layoutHeight();

    /**
     * Use this to set if this dialog fragment is cancelable onBackPressed
     * @return true if dialog is cancelable
     */
    public abstract boolean cancelable();

    @Override
    public void onStart() {
        super.onStart();
        if(!Value.IS.nullValue(getDialog())){
            getDialog().getWindow().setLayout(layoutWidth(), layoutHeight());
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * Use this method to get the fragments rootView
     * @return the fragments root view
     */
    public View getRootView(){
        return this.rootView;
    }

}
