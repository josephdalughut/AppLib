package ng.joey.lib.android.gui.view.textView;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import ng.joey.lib.android.R;
import ng.joey.lib.android.gui.util.FontUtils;

/**
 * Created by Joey Dalughut on 8/10/16 at 4:45 AM,
 * Project: Litigy Libraries.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */
public class TextView extends AppCompatTextView {

    public TextView(Context context) {
        super(context);
    }

    public TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyAttributes(attrs, this);
    }

    public TextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyAttributes(attrs, this);
    }

    public static void applyAttributes(AttributeSet attributeSet, android.widget.TextView textView){
        TypedArray array = textView.getContext().obtainStyledAttributes(attributeSet, R.styleable.TextView);
        int fontSize = array.getInt(R.styleable.TextView_fontSize, -1);
        int fontAppearance = array.getInt(R.styleable.TextView_fontAppearance, -1);
        int fontStyle = array.getInt(R.styleable.TextView_fontStyle, -1);
        if(fontSize!=-1){
            FontUtils.FontSize f = FontUtils.FontSize.values()[fontSize];
            FontUtils.applyFontSize(f, textView);
        }
        if(fontStyle!=-1){
            FontUtils.FontStyle f = FontUtils.FontStyle.values()[fontStyle];
            FontUtils.applyFontStyle(f, textView);
        }
        if(fontAppearance!=-1){
            FontUtils.FontSize appearance = FontUtils.FontSize.values()[fontAppearance];
            FontUtils.applyFontSize(appearance, textView);
            FontUtils.applyFontStyle(appearance, textView);
        }
        array.recycle();
    }

}
