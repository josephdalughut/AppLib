package ng.joey.lib.android.gui.dialog;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import ng.joey.lib.android.gui.view.button.Button;
import ng.joey.lib.android.R;
import ng.joey.lib.android.gui.view.textView.TextView;
import ng.joey.lib.java.util.Value;

/**
 * Created by Joey Dalughut on 8/13/16 at 1:11 PM,
 * Project: Skout.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */
public class InfoDialog extends DialogFragtivity {

    public static interface OnClickListener{
        public void onClick(View view, DialogFragtivity dialog);
    }

    public static InfoDialog getInstance(String title, String message, boolean cancelOnBack){
        return new InfoDialog().setTitle(title).setMessage(message)
                .setCancelOnBack(cancelOnBack);
    }


    public InfoDialog withPositiveButton(String string, final OnClickListener onClickListener){
        positiveMessage = string;
        positiveListener = onClickListener;
        setupPositiveButton();
        return this;
    }

    public InfoDialog withNegativeButton(String string, final OnClickListener onClickListener){
        negativeMessage = string;
        negativeListener = onClickListener;
        setupNegativeButton();
        return this;
    }

    public InfoDialog withNeutralButton(String string, final OnClickListener onClickListener){
        neutralMessage = string;
        neutralListener = onClickListener;
        setupNeutralButton();
        return this;
    }

    public InfoDialog withPositiveButton(String string, int textColor, final OnClickListener onClickListener){
        positiveMessage = string;
        positiveListener = onClickListener;
        positiveButtonTextColor = textColor;
        setupPositiveButton();
        return this;
    }

    public InfoDialog withNegativeButton(String string, int textColor, final OnClickListener onClickListener){
        negativeMessage = string;
        negativeListener = onClickListener;
        negativeButtonTextColor = textColor;
        setupNegativeButton();
        return this;
    }

    public InfoDialog withNeutralButton(String string, int textColor, final OnClickListener onClickListener){
        neutralMessage = string;
        neutralListener = onClickListener;
        this.neutralButtonTextColor = textColor;
        setupNeutralButton();
        return this;
    }

    OnClickListener positiveListener, negativeListener, neutralListener;
    Button positiveButton, negativeButton, neutralButton;
    private String title, message, positiveMessage, negativeMessage, neutralMessage;
    TextView titleTextView, messageTextView;
    ImageButton cancelButton;
    Integer negativeButtonTextColor, positiveButtonTextColor, neutralButtonTextColor;
    boolean cancelOnBack = true;

    public String getTitle() {
        return title;
    }

    public InfoDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public InfoDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public boolean isCancelOnBack() {
        return cancelOnBack;
    }

    public InfoDialog setCancelOnBack(boolean cancelOnBack) {
        this.cancelOnBack = cancelOnBack;
        return this;
    }

    @Override
    public int layoutId() {
        return R.layout.dialog_info;
    }


    @Override
    public void bundle(Bundle bundle) {

    }

    @Override
    public void findViews() {
        cancelButton = (ImageButton) findViewById(R.id.cancelButton);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        messageTextView = (TextView) findViewById(R.id.messageTextView);
        positiveButton = (Button) findViewById(R.id.positiveButton);
        negativeButton = (Button) findViewById(R.id.negativeButton);
        neutralButton = (Button) findViewById(R.id.neutralButton);
    }

    @Override
    public void setupViews() {
        titleTextView.setText(title);
        messageTextView.setText(message);
        getDialog().setCancelable(isCancelOnBack());
        setCancelable(isCancelOnBack());
        cancelButton.setAlpha(isCancelOnBack() ? 1f : 0f);
        cancelButton.setEnabled(isCancelOnBack());
        if(isCancelOnBack()){
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissAllowingStateLoss();
                }
            });
        }
        setupPositiveButton();
        setupNegativeButton();
        setupNeutralButton();
    }

    private void setupPositiveButton(){
        if(positiveButton == null)
            return;
        if(!Value.IS.nullValue(positiveButtonTextColor)){
            positiveButton.setTextColor(positiveButtonTextColor);
        }
        if(!Value.IS.emptyValue(positiveMessage) && !Value.IS.nullValue(positiveListener)){
            positiveButton.setVisibility(View.VISIBLE);
            positiveButton.setText(positiveMessage);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    positiveListener.onClick(view, InfoDialog.this);
                }
            });
        }else{
            positiveButton.setVisibility(View.INVISIBLE);
        }
    }

    private void setupNegativeButton(){
        if(negativeButton == null)
            return;
        if(!Value.IS.nullValue(negativeButtonTextColor)){
            negativeButton.setTextColor(negativeButtonTextColor);
        }
        if(!Value.IS.emptyValue(negativeMessage) && !Value.IS.nullValue(negativeListener)){
            negativeButton.setVisibility(View.VISIBLE);
            negativeButton.setText(negativeMessage);
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    negativeListener.onClick(view, InfoDialog.this);
                }
            });
        }else{
            negativeButton.setVisibility(View.INVISIBLE);
        }
    }

    private void setupNeutralButton(){
        if(neutralButton == null)
            return;
        if(!Value.IS.nullValue(neutralButtonTextColor)){
            neutralButton.setTextColor(neutralButtonTextColor);
        }
        if(!Value.IS.emptyValue(neutralMessage) && !Value.IS.nullValue(neutralListener)){
            neutralButton.setVisibility(View.VISIBLE);
            neutralButton.setText(neutralMessage);
            neutralButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    neutralListener.onClick(view, InfoDialog.this);
                }
            });
        }else{
            neutralButton.setVisibility(View.INVISIBLE);
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
