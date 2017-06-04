package ng.joey.lib.android.gui.view.textView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;

import ng.joey.lib.android.R;
import ng.joey.lib.java.util.Value;


public class SizeMatchingTextView extends TextView {

    private MatchBy matchBy = MatchBy.WIDTH;

    public SizeMatchingTextView(Context context) {
        super(context);
    }

    public SizeMatchingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SizeMatchingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //correctWidth();
        switch (matchBy){
            case HEIGHT:
                adjustHeight();
                break;
            case HALF_WIDTH:
                adjustWidthHalf();
                break;
            case HALF_HEIGHT:
                adjustHeightHalf();
                break;
            default:
                adjustWidth2();
                break;
        }

    }

    private void init(AttributeSet attributeSet){
        TypedArray a = getContext().obtainStyledAttributes(attributeSet, R.styleable.SizeMatchingTextView);
        matchBy = MatchBy.values()[a.getInt(R.styleable.SizeMatchingTextView_matchBy, MatchBy.WIDTH.ordinal())];
        a.recycle();
    }

    private void adjustWidth() {
        String text = getText().toString();
        if(Value.IS.emptyValue(text))
            return;
        int textViewWidth = getMeasuredWidth();
        //int textViewHeight = getMeasuredHeight();
        float textSize;
        Paint p = new Paint();
        Rect bounds = new Rect();
        p.setTextSize(1);
        p.getTextBounds(text, 0, text.length(), bounds);
        //float heightDifference = (textViewHeight);
        textSize = (float) ((textViewWidth)/bounds.width());
        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        setGravity(getGravity());
    }

    public void adjustWidth2() {
        float desiredWidth = getMeasuredWidth() * 0.8f;
        Paint paint = new Paint();
        Rect bounds = new Rect();

        paint.setTypeface(getTypeface());
        float textSize = desiredWidth;
        paint.setTextSize(textSize);
        String text = getText().toString();
        paint.getTextBounds(text, 0, text.length(), bounds);

        while (bounds.width() > desiredWidth)
        {
            textSize--;
            paint.setTextSize(textSize);
            paint.getTextBounds(text, 0, text.length(), bounds);
        }
        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        setGravity(getGravity());
    }

    public void adjustHeight() {
        int desiredHeight = getMeasuredHeight();
        Paint paint = new Paint();
        Rect bounds = new Rect();

        paint.setTypeface(getTypeface());
        float textSize = desiredHeight;
        paint.setTextSize(textSize);
        String text = getText().toString();
        paint.getTextBounds(text, 0, text.length(), bounds);

        while (bounds.height() > desiredHeight)
        {
            textSize--;
            paint.setTextSize(textSize);
            paint.getTextBounds(text, 0, text.length(), bounds);
        }
        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        setGravity(getGravity());
    }

    public void adjustWidthHalf() {
        int desiredWidth = getMeasuredWidth();
        Paint paint = new Paint();
        Rect bounds = new Rect();

        paint.setTypeface(getTypeface());
        float textSize = desiredWidth / 2;
        paint.setTextSize(textSize);
        String text = getText().toString();
        paint.getTextBounds(text, 0, text.length(), bounds);

        while (bounds.width() > desiredWidth)
        {
            textSize--;
            paint.setTextSize(textSize);
            paint.getTextBounds(text, 0, text.length(), bounds);
        }
        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        setGravity(getGravity());
    }

    public void adjustHeightHalf() {
        int desiredHeight = getMeasuredHeight();
        Paint paint = new Paint();
        Rect bounds = new Rect();

        paint.setTypeface(getTypeface());
        float textSize = desiredHeight / 2;
        paint.setTextSize(textSize);
        String text = getText().toString();
        paint.getTextBounds(text, 0, text.length(), bounds);

        while (bounds.height() > desiredHeight)
        {
            textSize--;
            paint.setTextSize(textSize);
            paint.getTextBounds(text, 0, text.length(), bounds);
        }
        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        setGravity(getGravity());
    }



//    public void correctWidth()
//    {
//        boolean width = getMeasuredWidth() > getMeasuredHeight();
//        int desiredWidth = width ? getMeasuredWidth() : getMeasuredHeight();
//        Paint paint = new Paint();
//        Rect bounds = new Rect();
//
//        paint.setTypeface(getTypeface());
//        float textSize = desiredWidth;
//        paint.setTextSize(textSize);
//        String text = getText().toString();
//        paint.getTextBounds(text, 0, text.length(), bounds);
//
//        while ((width ? bounds.width() : bounds.height()) > desiredWidth)
//        {
//            textSize--;
//            paint.setTextSize(textSize);
//            paint.getTextBounds(text, 0, text.length(), bounds);
//        }
//        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
//    }



    public static enum MatchBy {
        WIDTH, HEIGHT, HALF_WIDTH, HALF_HEIGHT
    }

}
