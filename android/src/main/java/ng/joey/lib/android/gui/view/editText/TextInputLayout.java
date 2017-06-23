package ng.joey.lib.android.gui.view.editText;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import ng.joey.lib.android.R;
import ng.joey.lib.android.gui.util.FontUtils;

/**
 * Created by joey on 6/12/17.
 */

public class TextInputLayout extends android.support.design.widget.TextInputLayout{
    public TextInputLayout(Context context) {
        super(context);
    }

    public TextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyAttributes(attrs, this);
    }

    public TextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyAttributes(attrs, this);
    }

    public static void applyAttributes(AttributeSet attributeSet, android.support.design.widget.TextInputLayout textInputLayout){
        TypedArray array = textInputLayout.getContext().obtainStyledAttributes(attributeSet, R.styleable.TextView);
        int fontStyle = array.getInt(R.styleable.TextView_fontStyle, -1);
        if(fontStyle!=-1){
            FontUtils.FontStyle f = FontUtils.FontStyle.values()[fontStyle];
            FontUtils.applyFontStyle(f, textInputLayout);
        }
        array.recycle();
    }


}
