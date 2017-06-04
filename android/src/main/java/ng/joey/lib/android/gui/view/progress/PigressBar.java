package ng.joey.lib.android.gui.view.progress;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import ng.joey.lib.android.R;

/**
 * Created by thgriefers on 9/23/16.
 */

public class PigressBar extends FrameLayout {
    private float progress = 0.0F;
    private float max = 100.0F;
    private boolean isIndeterminate = false;
    private int indeterminateDirection = 1;
    private Paint paint = new Paint();
    private Paint paintOverlay = new Paint();
    private RectF rectF = new RectF();
    private RectF rectOverlay = new RectF();
    private ValueAnimator animator;
    private int progressDuration = 3000;

    public PigressBar(Context context) {
        super(context);
        this.init((AttributeSet)null);
    }

    public PigressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs);
    }

    public PigressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PigressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        this.paint.setAntiAlias(true);
        this.paintOverlay.setAntiAlias(true);
        this.setWillNotDraw(false);
        if(attributeSet != null) {
            TypedArray a = this.getContext().obtainStyledAttributes(attributeSet, R.styleable.PigressBar);
            this.setColor(a.getColor(R.styleable.PigressBar_pigressColor, getResources().getColor(R.color.flat_asbestos)));
            this.setProgress(a.getFloat(R.styleable.PigressBar_pigressProgress, this.progress));
            this.setMax(a.getFloat(R.styleable.PigressBar_pigressMax, this.max));
            this.setIndeterminate(a.getBoolean(R.styleable.PigressBar_pigressIndeterminate, true));
            this.setDuration(a.getInteger(R.styleable.PigressBar_pigressDuration, this.progressDuration));
            a.recycle();
        }
    }

    public void setIndeterminate(boolean indeterminate) {
        this.isIndeterminate = indeterminate;
        if(this.isIndeterminate()) {
            this.startIndeterminate();
        } else {
            this.stopIndeterminate();
        }

        this.invalidate();
    }

    public void setDuration(int duration) {
        this.progressDuration = duration;
    }

    private void startIndeterminate() {
        if(isInEditMode())
            return;
        this.getAnimator().start();
    }

    public void invalidate() {
        if(!this.isInEditMode()) {
            super.invalidate();
        }
    }

    private void stopIndeterminate() {
        if(isInEditMode())
            return;
        if(this.animator != null) {
            this.getAnimator().cancel();
            this.animator = null;
        }

    }

    private ValueAnimator getAnimator() {
        return this.animator == null?this.buildAnimator():this.animator;
    }

    private ValueAnimator buildAnimator() {
        float start = getProgress();
        float end = this.getMax() - progress;
        this.animator = ObjectAnimator.ofFloat(this, "progress", start, end);
        this.animator.setRepeatMode(ValueAnimator.RESTART);
        this.animator.setInterpolator(new AccelerateDecelerateInterpolator());
        this.animator.addListener(new Animator.AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
               PigressBar.this.indeterminateDirection = PigressBar.this.indeterminateDirection > 0?-1:1;
            }
        });
        this.animator.setDuration((long)this.progressDuration);
        this.animator.setRepeatCount(-1);
        return this.animator;
    }

    public void setProgress(float progress) {
        if(progress > this.max) {
            this.max = progress;
        }

        this.progress = progress;
        this.invalidate();
    }

    public float getProgress() {
        return this.progress;
    }

    public float getMax() {
        return this.max;
    }

    public void setMax(float max) {
        if(this.progress > max) {
            this.progress = max;
        }

        this.max = max;
        this.invalidate();
    }

    public void setColor(int color) {
        if(this.paint != null) {
            this.paint.setColor(color);
            this.paintOverlay.setColor(color);
            this.paintOverlay.setAlpha(85);
            this.invalidate();
        }
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        float left = 0.0F;
        float right = (float)width;
        float top = 0.0F;
        float bottom = (float)height;
        float startAngle;
        float sweepAngle;
        if(this.isIndeterminate() && this.indeterminateDirection != 1) {
            float angle = this.getProgress() / this.getMax() * 360.0F;
            sweepAngle = 360.0F - angle;
            startAngle = -90.0F + angle;
        } else {
            startAngle = -90.0F;
            sweepAngle = this.getProgress() / this.getMax() * 360.0F;
        }

        this.rectF.set(left, top, right, bottom);
        this.rectOverlay.set(left, top, right, bottom);
        canvas.drawCircle((float)(height / 2), (float)(width / 2), (float)(height > width?height / 2:width / 2), this.paintOverlay);
        canvas.drawArc(this.rectF, startAngle, sweepAngle, true, this.paint);
    }

    public boolean isIndeterminate() {
        return this.isIndeterminate;
    }
}
