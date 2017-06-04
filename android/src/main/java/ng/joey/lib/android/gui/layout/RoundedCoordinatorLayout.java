package ng.joey.lib.android.gui.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import ng.joey.lib.android.R;
import ng.joey.lib.java.util.Value;

/**
 * Created by Joey Dalu.
 * https://josephdalughut.github.io/
 * Project: Chitchat,
 * created at 10:46 AM, December 26 2016.
 */

/**
 * A frame layout with a custom background that is drawn with rounded corners
 */
public class RoundedCoordinatorLayout extends CoordinatorLayout {

    /**
     * Default x-radius color the oval used to draw this views background
     */
    public static final int DEFAULT_RADIUS_TOP_LEFT = 0;
    public static final int DEFAULT_RADIUS_TOP_RIGHT = 0;

    /**
     * Default y-radius color the oval used to draw this views background
     */
    public static final int DEFAULT_RADIUS_BOTTOM_LEFT = 0;
    public static final int DEFAULT_RADIUS_BOTTOM_RIGHT = 0;

    /**
     * Default color in which this views background would be drawn (WHITE)
     */
    public static final int DEFAULT_BACKGROUND_COLOR = 0xFFFFFFFF;

    /**
     * The x-radius color the oval used to draw this views background
     */
    private int radiusTopLeft = DEFAULT_RADIUS_TOP_LEFT;
    private int radiusTopRight = DEFAULT_RADIUS_TOP_RIGHT;
    /**
     * The y-radius color the oval used to draw this views background
     */
    private int radiusBottomLeft = DEFAULT_RADIUS_BOTTOM_LEFT;
    private int radiusBottomRight = DEFAULT_RADIUS_BOTTOM_RIGHT;

    /**
     * The color in which this views background would be drawn
     */
    private int backgroundColor = DEFAULT_BACKGROUND_COLOR;

    /**
     * to hold our left,top,right and bottom values so that we don't have to create a new rect instance
     * every time onDraw is called
     */
    private RectF rect;
    /**
     * paint which draws background on our canvas
     */
    private Paint paint;

    private Path path;

    private boolean clipPath = false;

    /**
     * Default constructor
     */
    public RoundedCoordinatorLayout(Context context) {
        super(context);
    }

    public RoundedCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupAttributes(attrs);
    }

    public RoundedCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupAttributes(attrs);
    }

    /**
     * Setup this views attributes
     * @param attrs set color attributes to setup this view with
     */
    private void setupAttributes(AttributeSet attrs){
        setWillNotDraw(false);
        paint = new Paint();
        paint.setAntiAlias(true);
        path = new Path();
        rect = new RectF();
        if(Value.IS.nullValue(attrs))
            return;
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.RoundedFrameLayout);
        backgroundColor = array.getColor(R.styleable.RoundedFrameLayout_backgroundColor, DEFAULT_BACKGROUND_COLOR);
        paint.setColor(backgroundColor);
        radiusTopLeft = array.getDimensionPixelSize(R.styleable.RoundedFrameLayout_radiusTopLeft, DEFAULT_RADIUS_TOP_LEFT);
        radiusTopRight = array.getDimensionPixelSize(R.styleable.RoundedFrameLayout_radiusTopRight, DEFAULT_RADIUS_TOP_RIGHT);
        radiusBottomLeft = array.getDimensionPixelSize(R.styleable.RoundedFrameLayout_radiusBottomLeft, DEFAULT_RADIUS_BOTTOM_LEFT);
        radiusBottomRight = array.getDimensionPixelSize(R.styleable.RoundedFrameLayout_radiusBottomRight, DEFAULT_RADIUS_BOTTOM_RIGHT);
        clipPath = array.getBoolean(R.styleable.RoundedFrameLayout_clipPath, false);
        array.recycle();
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.reset();
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        path = getRoundedRect(0, 0, width, height, radiusTopLeft * 2, radiusTopRight * 2, radiusBottomLeft * 2, radiusBottomRight * 2);
        canvas.drawPath(path, paint);
        if(clipPath)
            canvas.clipPath(path, Region.Op.INTERSECT);
    }


    public int getRadiusTopLeft() {
        return radiusTopLeft;
    }

    public RoundedCoordinatorLayout setRadiusTopLeft(int radiusTopLeft) {
        this.radiusTopLeft = radiusTopLeft;
        invalidate();
        return this;
    }

    public int getRadiusBottomLeft() {
        return radiusBottomLeft;
    }

    public RoundedCoordinatorLayout setRadiusBottomLeft(int radiusBottomLeft) {
        this.radiusBottomLeft = radiusBottomLeft;
        invalidate();
        return this;
    }

    public int getRadiusTopRight() {
        return radiusTopRight;
    }

    public RoundedCoordinatorLayout setRadiusTopRight(int radiusTopRight) {
        this.radiusTopRight = radiusTopRight;
        invalidate();
        return this;
    }

    public int getRadiusBottomRight() {
        return radiusBottomRight;
    }

    public RoundedCoordinatorLayout setRadiusBottomRight(int radiusBottomRight) {
        this.radiusBottomRight = radiusBottomRight;
        invalidate();
        return this;
    }

    /**
     * @return the color in which this views background would be drawn
     */
    public int getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * @param backgroundColor the color in which this views background would be drawn
     */
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        if(!Value.IS.nullValue(paint))
            paint.setColor(backgroundColor);
        invalidate();
    }

    public Path getRoundedRect(
            float left, float top, float right, float bottom, float topLeftRadius,
            float topRightRadius, float bottomLeftRadius, float bottomRightRadius){
        Path path = new Path();
        if (topLeftRadius < 0) topLeftRadius = 0;
        if (topRightRadius < 0) topRightRadius = 0;
        if (bottomLeftRadius < 0) bottomLeftRadius = 0;
        if (bottomRightRadius < 0) bottomRightRadius = 0;


        path.moveTo(right, top + topRightRadius);
        rect.set(right - topRightRadius, top, right, top + topRightRadius);
        path.arcTo(rect, 0, -90, true);

        path.lineTo(left+topLeftRadius, top);
        rect.set(left, top, topLeftRadius, topLeftRadius);
        path.arcTo(rect, 270, -90);

        path.lineTo(left, bottom - bottomLeftRadius);
        rect.set(left, bottom - bottomLeftRadius, left + bottomLeftRadius, bottom);
        path.arcTo(rect, 180, -90);

        path.lineTo(right - bottomRightRadius, bottom);
        rect.set(right - bottomRightRadius, bottom - bottomRightRadius, right, bottom);
        path.arcTo(rect, 90, -90);
        path.lineTo(right, top+topRightRadius);

        /*

        path.moveTo(right, top + trr);
        //path.rQuadTo(0, -trr, -trr, -trr);//top-right corner
        path.rLineTo(left + tlr, top);
        //path.rQuadTo(-tlr, 0, -tlr, tlr); //top-left corner
        path.rLineTo(left, height - blr);
        //path.rQuadTo(0, blr, blr, blr);//bottom-left corner
        path.rLineTo(right - brr, height);
        //path.rQuadTo(brr, 0, brr, -brr); //bottom-right corner
        path.rLineTo(right, top+trr);
        */

        path.close();
        return path;
    }

}
