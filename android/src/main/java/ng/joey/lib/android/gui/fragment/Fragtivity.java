package ng.joey.lib.android.gui.fragment;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import butterknife.ButterKnife;

/**
 * Created by Joey Dalughut on 8/10/16 at 5:30 PM,
 * Project: Litigy Libraries.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */
public abstract class Fragtivity extends Fragment implements ViewTreeObserver.OnGlobalLayoutListener {

    private View rootView;
    private boolean isKeyboardShowing = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView(inflater.inflate(layoutId(), container, false));
        if(shouldWatchKeyboard())
            getRootView().getViewTreeObserver().addOnGlobalLayoutListener(this);
        ButterKnife.bind(this, rootView);
        findViews();
        setupViews();
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(shouldWatchKeyboard()) {
            try {
                if (Build.VERSION.SDK_INT >= 16) {
                    getRootView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    getRootView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            } catch (Exception e) {

            }
        }
    }

    /**
     * Use this method to get the fragments rootView
     * @return the fragments root view
     */
    public View getRootView(){
        return this.rootView;
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
    public void bundle(Bundle bundle){}

    /**
     * Called after the fragment has been created and its view inflated
     */
    public abstract void findViews();

    /**
     * Called after findViews()
     */
    public abstract void setupViews();

    /**
     * Called when the keyboard shown
     * @param height the height of the keyboard in dp
     */
    public void onKeyboardShown(int height){}

    /**
     * Called when the keyboard is hidden
     */
    public void onKeyboardHidden(){}

    /**
     * Set that this fragment should watch for keyboard visibility changes. If true, then methods
     * onKeyboardShown(int height) and onKeyboardHidden() would be called
     * @return
     */
    public boolean shouldWatchKeyboard(){
        return false;
    }

    @Override
    public void onGlobalLayout() {
        Rect r = new Rect();
        getRootView().getWindowVisibleDisplayFrame(r);
        int screenHeight = getRootView().getRootView().getHeight();
        int keypadHeight = screenHeight - r.bottom;
        if (keypadHeight > screenHeight * 0.15) {
            if(!isKeyboardShowing) {
                isKeyboardShowing = true;
                onKeyboardShown(keypadHeight);
            }
        }
        else {
            if(isKeyboardShowing) {
                isKeyboardShowing = false;
                onKeyboardHidden();
            }
        }
    }

    @Override
    public void onSaveInstanceState( Bundle outState ) {

    }

}