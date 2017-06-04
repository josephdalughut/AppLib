package ng.joey.lib.android.gui.view.editText;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import ng.joey.lib.android.gui.view.textView.TextView;

/**
 * Created by Joey Dalughut on 8/10/16 at 5:02 AM,
 * Project: Litigy Libraries.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */
public class EditText extends AppCompatEditText {
    public EditText(Context context) {
        super(context);
    }

    public EditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TextView.applyAttributes(attrs, this);
    }

    public EditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TextView.applyAttributes(attrs, this);
    }



}
