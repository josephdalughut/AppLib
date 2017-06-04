package ng.joey.lib.android.gui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import ng.joey.lib.android.gui.util.ViewUtils;
import ng.joey.lib.android.gui.view.progress.PigressBar;
import ng.joey.lib.android.R;
import ng.joey.lib.android.gui.view.textView.TextView;
import ng.joey.lib.java.util.Value;

/**
 * Created by Joey Dalughut on 8/13/16 at 1:11 PM,
 * Project: Skout.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */
public class ProgressDialog extends DialogFragtivity {

    public static ProgressDialog getInstance(String title, String message, boolean cancelOnBack){
        return new ProgressDialog().setTitle(title).setMessage(message)
                .setCancelOnBack(cancelOnBack);
    }

    public static ProgressDialog getInstance(Context context, int title, int message,
                                             boolean cancelOnBack){
        return new ProgressDialog().setTitle(context.getString(title))
                .setMessage(context.getString(message)).setCancelOnBack(cancelOnBack);
    }

    public static ProgressDialog getInstance(String title, String message, int progressColor, boolean cancelOnBack){
        return new ProgressDialog().setTitle(title).setMessage(message)
                .setProgressColor(progressColor)
                .setCancelOnBack(cancelOnBack);
    }

    public static ProgressDialog getInstance(Context context, int title, int message,
                                             int progressColor, boolean cancelOnBack){
        return new ProgressDialog().setTitle(context.getString(title))
                .setProgressColor(progressColor)
                .setMessage(context.getString(message)).setCancelOnBack(cancelOnBack);
    }

    private String title, message;
    Integer progressColor;
    TextView titleTextView, messageTextView;
    ProgressBar barProgress;
    ImageButton cancelButton;
    boolean cancelOnBack = true;

    public String getTitle() {
        return title;
    }

    public ProgressDialog setProgressColor(Integer progressColor) {
        this.progressColor = progressColor;
        return this;
    }

    public ProgressDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ProgressDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public boolean isCancelOnBack() {
        return cancelOnBack;
    }

    public ProgressDialog setCancelOnBack(boolean cancelOnBack) {
        this.cancelOnBack = cancelOnBack;
        return this;
    }

    public ProgressBar getProgress() {
        return barProgress;
    }

    @Override
    public int layoutId() {
        return R.layout.dialog_dialog_progress;
    }

    @Override
    public void bundle(Bundle bundle) {

    }

    @Override
    public void findViews() {
        cancelButton = (ImageButton) findViewById(R.id.cancelButton);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        messageTextView = (TextView) findViewById(R.id.messageTextView);
        barProgress = (ProgressBar) findViewById(R.id.barProgress);
    }

    @Override
    public void setupViews() {
        titleTextView.setText(title);
        messageTextView.setText(message);
        getDialog().setCancelable(isCancelOnBack());
        setCancelable(isCancelOnBack());
        cancelButton.setAlpha(isCancelOnBack() ? 1f : 0f);
        if(!Value.IS.nullValue(progressColor))
            ViewUtils.setColor(progressColor, barProgress);
        cancelButton.setEnabled(isCancelOnBack());
        if(isCancelOnBack()){
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissAllowingStateLoss();
                }
            });
        }
    }

    @Override
    public int layoutWidth() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    public int layoutHeight() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    public boolean cancelable() {
        return false;
    }
}
