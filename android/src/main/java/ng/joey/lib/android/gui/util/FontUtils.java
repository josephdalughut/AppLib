package ng.joey.lib.android.gui.util;

import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.util.TypedValue;
import android.widget.TextView;

import ng.joey.lib.android.R;
import ng.joey.lib.java.util.Value;

/**
 * Created by Joey Dalughut on 8/9/16 at 11:36 AM.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */
public class FontUtils {

    /**
     * Enum set for the Lato font set. Each enum has the name of its asset file as the field
     * 'assetName'.
     */
    public static enum FontStyle {
        bold("bold.ttf"), boldItalic("boldItalic.ttf"),
        italic("italic.ttf"), light("lightItalic.ttf"),
        regular("regular.ttf"), semiBold("semiBold.ttf"),
        semiBoldItalic("semiBoldItalic.ttf"), thin("thin.ttf"),
        thinItalic("thinItalic.ttf");

        String assetName;

        FontStyle(String assetName){
            this.assetName = assetName;
        }

        public String getAssetName(){
            return assetName;
        }
    }

    /**
     * Enum set for font size. Each enum has the dimension resource id of its size as the field
     * 'dimenRes'
     */
    public static enum FontSize {
        caption(R.dimen.text_size_caption, FontStyle.regular),
        body1(R.dimen.text_size_body1, FontStyle.regular),
        body2(R.dimen.text_size_body2, FontStyle.bold),
        button(R.dimen.text_size_button, FontStyle.bold),
        subhead(R.dimen.text_size_subHead, FontStyle.regular),
        title(R.dimen.text_size_title, FontStyle.bold),
        headline(R.dimen.text_size_headline, FontStyle.regular),
        display1(R.dimen.text_size_display1, FontStyle.regular),
        display2(R.dimen.text_size_display2, FontStyle.regular),
        display3(R.dimen.text_size_display3, FontStyle.regular),
        display4(R.dimen.text_size_display4, FontStyle.light);

        int dimenRes;
        FontStyle fontStyle;

        FontSize(int dimenRes, FontStyle fontStyle){
            this.dimenRes = dimenRes;
            this.fontStyle = fontStyle;
        }

        public int getDimenRes(){
            return dimenRes;
        }

        public FontStyle getFontStyle(){
            return fontStyle;
        }

    }

    /**
     * Apply a fontSize to a {@link TextView}
     * @param fontSize the font size to set
     * @param textView the {@link TextView}
     * @return {@param textView} with fontStyle set to {@param fontSize}
     */
    public static TextView applyFontSize(FontSize fontSize, TextView textView){
        if(Value.IS.ANY.nullValue(textView, fontSize))
            return textView;
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textView.getContext().getResources().getDimensionPixelSize(
                fontSize.getDimenRes()));
        return textView;
    }

    /**
     * Apply a fontStyle to a {@link TextView}
     * @param fontStyle the font style to set
     * @param textView the {@link TextView}
     * @return {@param textView} with fontStyle set to {@param fontStyle}
     */
    public static TextView applyFontStyle(FontStyle fontStyle, TextView textView){
        if(Value.IS.ANY.nullValue(textView, fontStyle)||textView.isInEditMode())
            return textView;
        if(!textView.isInEditMode())
        textView.setTypeface(Typeface.createFromAsset(textView.getContext().getAssets(),
                fontStyle.getAssetName()));
        return textView;
    }

    /**
     * Apply a fontStyle to a {@link TextView}
     * @param fontSize the font size to set
     * @param textView the {@link TextView}
     * @return {@param textView} with fontStyle set to {@param fontStyle}
     */
    public static TextView applyFontStyle(FontSize fontSize, TextView textView){
        if(Value.IS.ANY.nullValue(textView, fontSize))
            return textView;
        if(!textView.isInEditMode())
        textView.setTypeface(Typeface.createFromAsset(textView.getContext().getAssets(),
                fontSize.getFontStyle().getAssetName()));
        return textView;
    }

    /**
     * Apply both a fontStyle and an fontAppearance to a {@link TextView}
     * @param fontSize the font size
     * @param textView the {@link TextView}
     * @return {@param textView} with fontStyle and fontSize set to those determined by
     * {@param fontSize}
     */
    public static TextView applyFontAppearance(FontSize fontSize, TextView textView){
        if(Value.IS.ANY.nullValue(textView, fontSize))
            return textView;
        if(!textView.isInEditMode())
        textView.setTypeface(Typeface.createFromAsset(textView.getContext().getAssets(),
                fontSize.getFontStyle().getAssetName()));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textView.getResources().getDimensionPixelSize(fontSize.getDimenRes()));
        return textView;
    }

    /**
     * Apply a fontStyle to a {@link TextView}
     * @param fontStyle the font style to set
     * @param textInputLayout the {@link TextView}
     * @return {@param textView} with fontStyle set to {@param fontStyle}
     */
    public static TextInputLayout applyFontStyle(FontStyle fontStyle, TextInputLayout textInputLayout){
        if(Value.IS.ANY.nullValue(textInputLayout, fontStyle)||textInputLayout.isInEditMode())
            return textInputLayout;
        if(!textInputLayout.isInEditMode())
            textInputLayout.setTypeface(Typeface.createFromAsset(textInputLayout.getContext().getAssets(),
                    fontStyle.getAssetName()));
        return textInputLayout;
    }

}

