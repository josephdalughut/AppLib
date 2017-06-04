package ng.joey.lib.android.gui.layout;

/**
 * Created by root on 3/2/17.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import ng.joey.lib.android.R;
import ng.joey.lib.android.gui.util.FontUtils;

public class TabLayout extends android.support.design.widget.TabLayout {

    public TabLayout(Context context) {
        super(context);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private int fontSize, fontAppearance, fontStyle;
    private boolean allCaps;

    @Override
    public void addTab(Tab tab) {
        super.addTab(tab);

        ViewGroup mainView = (ViewGroup) getChildAt(0);
        ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());

        int tabChildCount = tabView.getChildCount();
        for (int i = 0; i < tabChildCount; i++) {
            View tabViewChild = tabView.getChildAt(i);
            if (tabViewChild instanceof TextView) {
                TextView textView = (TextView) tabViewChild;
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
                textView.setAllCaps(allCaps);
            }
        }
    }


    public void init(AttributeSet attributeSet){
        TypedArray array = getContext().obtainStyledAttributes(attributeSet, R.styleable.TextView);
        TypedArray array2 = getContext().obtainStyledAttributes(attributeSet, R.styleable.TabLayout);
        fontSize = array.getInt(R.styleable.TextView_fontSize, -1);
        fontAppearance = array.getInt(R.styleable.TextView_fontAppearance, -1);
        fontStyle = array.getInt(R.styleable.TextView_fontStyle, -1);
        allCaps = array2.getBoolean(R.styleable.TabLayout_allCaps, false);
        array.recycle();
        array2.recycle();
    }
}