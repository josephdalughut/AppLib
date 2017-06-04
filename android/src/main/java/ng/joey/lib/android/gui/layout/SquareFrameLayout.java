package ng.joey.lib.android.gui.layout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import ng.joey.lib.android.R;


/**
 * Created by root on 4/6/17.
 */

public class SquareFrameLayout extends FrameLayout {

    private SquareBy squareBy = SquareBy.WIDTH;

    public SquareFrameLayout(Context context) {
        super(context);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SquareFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(squareBy == SquareBy.WIDTH) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }else{
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        }
    }

    private void init(AttributeSet attributeSet){
        TypedArray a = getContext().obtainStyledAttributes(attributeSet, R.styleable.SquareFrameLayout);
        squareBy = SquareBy.values()[a.getInt(R.styleable.SquareFrameLayout_squareBy, 0)];
        a.recycle();
    }

    public enum SquareBy {
        WIDTH, HEIGHT
    }

}
