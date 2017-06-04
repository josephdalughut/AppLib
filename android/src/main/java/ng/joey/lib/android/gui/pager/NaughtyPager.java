package ng.joey.lib.android.gui.pager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class NaughtyPager extends ViewPager {

    private boolean isPagingEnabled = false;

    public NaughtyPager(Context context) {
        super(context);
    }

    public NaughtyPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isPagingEnabled() && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isPagingEnabled() && super.onTouchEvent(ev);
    }

    public boolean isPagingEnabled() {
        return isPagingEnabled;
    }

    public NaughtyPager setPagingEnabled(boolean pagingEnabled) {
        isPagingEnabled = pagingEnabled;
        return this;
    }
}
