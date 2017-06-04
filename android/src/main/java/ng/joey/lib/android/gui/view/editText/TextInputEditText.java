package ng.joey.lib.android.gui.view.editText;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import ng.joey.lib.android.gui.view.textView.TextView;

public class TextInputEditText extends android.support.design.widget.TextInputEditText {
    public TextInputEditText(Context context) {
        super(context);
    }

    public TextInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TextView.applyAttributes(attrs, this);
    }

    public TextInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TextView.applyAttributes(attrs, this);
    }



}
