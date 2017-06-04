package ng.joey.lib.android.gui.view.button;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import ng.joey.lib.android.gui.view.textView.TextView;

/**
 * Created by Joey Dalughut on 8/10/16 at 5:00 AM,
 * Project: Litigy Libraries.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */
public class Button extends AppCompatButton {
    public Button(Context context) {
        super(context);
    }

    public Button(Context context, AttributeSet attrs) {
        super(context, attrs);
        TextView.applyAttributes(attrs, this);
    }

    public Button(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TextView.applyAttributes(attrs, this);
    }

}
