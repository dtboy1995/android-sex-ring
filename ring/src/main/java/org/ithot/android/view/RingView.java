package org.ithot.android.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import org.ithot.android.R;
import org.ithot.android.view.listener.AVBaseCallback;

public class RingView extends View {

    public static final String TAG = "[SexRing]";

    private static final String ANIMATE_PROPERTY = "progress";
    public static final float MAX_PROGRESS = 100f;
    private static final int DEFAULT_DURATION = 2000;
    private static final int DEFAULT_START_ANGLE = 0;
    private static final int DEFAULT_SWEEP_ANGLE = 360;
    private static final int DEFAULT_STROKE_WIDTH = 4;
    private static final int INTERPOLATOR_LINEAR = 0;
    private static final int INTERPOLATOR_ACCELERATE = 1;
    private static final int INTERPOLATOR_DECELERATE = 2;
    private static final int CAP_ROUND = 0;
    private static final int CAP_BUTT = 1;
    private static final int CAP_SQUARE = 2;
    private static final int DEFAULT_SHADOW_RADIUS = 8;

    private static final int DEFALUT_BACKGROUND_COLOR = Color.parseColor("#9E9E9E");
    private static final int DEFALUT_FOREGROUND_COLOR = Color.parseColor("#2195F2");
    private static final int DEFAULT_SHADOW_COLOR = Color.parseColor("#374046");

    private static final Class<? extends TimeInterpolator> DEFAULT_INTERPOLATOR = LinearInterpolator.class;
    private boolean DEBUG = false;

    private Paint backgroundPaint;
    private Paint foregroundPaint;
    private RectF ovalRectF;
    private int progress;
    private int ovalIndent;

    private Class<? extends TimeInterpolator> interpolator;
    private int animateDuration;
    private int startAngle;
    private int sweepAngle;
    private boolean canGo = true;
    private AVBaseCallback callback;
    private boolean touchable;
    private int longPressTimer = 500;
    private long timer;
    private float preX;
    private float preY;

    public RingView(Context context) {
        super(context);
        init(null);
    }

    public RingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    void init(AttributeSet attrs) {
        backgroundPaint = new Paint();
        foregroundPaint = new Paint();
        ovalRectF = new RectF();
        int strokeWidth;
        Paint.Cap cap;
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.RingView);
            strokeWidth = (int) ta.getDimension(R.styleable.RingView_strokeWidth, dp2px(DEFAULT_STROKE_WIDTH));
            startAngle = ta.getInteger(R.styleable.RingView_startAngle, DEFAULT_START_ANGLE);
            sweepAngle = ta.getInteger(R.styleable.RingView_sweepAngle, DEFAULT_SWEEP_ANGLE);
            backgroundPaint.setColor(ta.getColor(R.styleable.RingView_backgroundColor, DEFALUT_BACKGROUND_COLOR));
            foregroundPaint.setColor(ta.getColor(R.styleable.RingView_foregroundColor, DEFALUT_FOREGROUND_COLOR));
            animateDuration = ta.getInteger(R.styleable.RingView_animateDuration, DEFAULT_DURATION);
            touchable = ta.getBoolean(R.styleable.RingView_touchable, false);
            boolean shadowEnable = ta.getBoolean(R.styleable.RingView_shadowEnable, false);
            int _shadow = ta.getInt(R.styleable.RingView_shadowRadius, DEFAULT_SHADOW_RADIUS);
            if (shadowEnable) {
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                backgroundPaint.setShadowLayer(_shadow, 0, 0,
                        ta.getColor(R.styleable.RingView_shadowColor, DEFAULT_SHADOW_COLOR));
            } else {
                setLayerType(View.LAYER_TYPE_HARDWARE, null);
            }
            ovalIndent = (int) Math.ceil((strokeWidth) / 2f + _shadow);

            int _interpolator = ta.getInteger(R.styleable.RingView_animateType, INTERPOLATOR_LINEAR);
            switch (_interpolator) {
                case INTERPOLATOR_LINEAR:
                    interpolator = LinearInterpolator.class;
                    break;
                case INTERPOLATOR_ACCELERATE:
                    interpolator = AccelerateInterpolator.class;
                    break;
                case INTERPOLATOR_DECELERATE:
                    interpolator = DecelerateInterpolator.class;
                    break;
            }
            int _cap = ta.getInt(R.styleable.RingView_strokeCap, CAP_ROUND);
            switch (_cap) {
                case CAP_ROUND:
                    cap = Paint.Cap.ROUND;
                    break;
                case CAP_BUTT:
                    cap = Paint.Cap.BUTT;
                    break;
                case CAP_SQUARE:
                    cap = Paint.Cap.SQUARE;
                    break;
                default:
                    cap = Paint.Cap.ROUND;
            }
            ta.recycle();
        } else {
            cap = Paint.Cap.ROUND;
            strokeWidth = dp2px(DEFAULT_STROKE_WIDTH);
            startAngle = DEFAULT_START_ANGLE;
            sweepAngle = DEFAULT_SWEEP_ANGLE;
            backgroundPaint.setColor(DEFALUT_BACKGROUND_COLOR);
            foregroundPaint.setColor(DEFALUT_FOREGROUND_COLOR);
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
            ovalIndent = (int) Math.ceil((strokeWidth) / 2f);
            interpolator = DEFAULT_INTERPOLATOR;
            animateDuration = DEFAULT_DURATION;
        }

        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(strokeWidth);
        backgroundPaint.setStrokeCap(cap);

        foregroundPaint.setAntiAlias(true);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeWidth(strokeWidth);
        foregroundPaint.setStrokeCap(cap);
    }

    public int getProgress() {
        return progress;
    }

    public void setCallback(AVBaseCallback callback) {
        this.callback = callback;
    }

    public void setProgress(int progress) {
        if (progress > MAX_PROGRESS) {
            progress = (int) MAX_PROGRESS;
        }
        this.progress = progress;
        if (callback != null) {
            callback.call(progress);
        }
        invalidate();
    }

    public void setInterpolator(Class<? extends TimeInterpolator> interpolator) {
        this.interpolator = interpolator;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                timer = System.currentTimeMillis();
                setPressed(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                boolean isInside = (x > 0 && x < getWidth() && y > 0 && y < getHeight());
                if (isPressed() != isInside) {
                    setPressed(isInside);
                }
                if (Math.abs(x - preX) > 10 || Math.abs(y - preY) > 10) {
                    if (touchable) {
                        supply(event);
                    }
                } else {
                    preX = x;
                    preY = y;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                setPressed(false);
                break;
            case MotionEvent.ACTION_UP:
                if (isPressed()) {
                    setPressed(false);
                    if (System.currentTimeMillis() - timer > longPressTimer) {
                        performLongClick();
                    } else {
                        performClick();
                    }
                }
                break;
        }
        return true;
    }

    public int getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(int startAngle) {
        this.startAngle = startAngle;
    }

    public int getSweepAngle() {
        return sweepAngle;
    }

    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }

    public void setLongPressTimer(int longPressTimer) {
        this.longPressTimer = longPressTimer;
    }

    public void setSweepAngle(int sweepAngle) {
        this.sweepAngle = sweepAngle;
    }

    public void go(int progress, boolean animate) {
        if (this.progress == progress) {
            return;
        }
        if (animate) {
            if (!canGo) {
                return;
            }
            int max = Math.max(this.progress, progress);
            int min = Math.min(this.progress, progress);
            int _duration = (int) ((this.animateDuration * (max - min)) / MAX_PROGRESS);
            ObjectAnimator animator = ObjectAnimator.ofInt(this,
                    ANIMATE_PROPERTY, this.progress, progress);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    canGo = true;
                }
            });
            animator.setDuration(_duration);
            try {
                animator.setInterpolator(interpolator.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
            canGo = false;
            animator.start();
        } else {
            setProgress(progress);
        }
    }

    public void go(int progress) {
        go(progress, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(ovalRectF, startAngle, sweepAngle, false, backgroundPaint);
        canvas.drawArc(ovalRectF, startAngle, calDegree(), false, foregroundPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int _width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int _height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int min = Math.min(_width, _height);
        setMeasuredDimension(min, min);
        ovalRectF.set(ovalIndent + getPaddingLeft(),
                ovalIndent + getPaddingTop(),
                getMeasuredWidth() - ovalIndent - getPaddingRight(),
                getMeasuredHeight() - ovalIndent - getPaddingBottom());
    }

    private void debug(Object trace) {
        if (DEBUG) {
            Log.d(TAG, trace.toString());
        }
    }

    private void supply(MotionEvent event) {
        int width = (int) ovalRectF.width();
        int height = (int) ovalRectF.height();
        int halfW = width / 2;
        int halfH = height / 2;
        int centerX = (int) ovalRectF.centerX();
        int centerY = (int) ovalRectF.centerY();
        float x = event.getX();
        float y = event.getY();
        int distance = (int) Math.sqrt(Math.pow(y - centerY, 2) + Math.pow(x - centerX, 2));
        if (distance > halfW + foregroundPaint.getStrokeWidth() || distance < halfW - foregroundPaint.getStrokeWidth() * 5) {
            return;
        }
        //
        double angle = Math.abs(Math.toDegrees(Math.atan((y - centerY) / (x - centerX))));
        if (x >= 0 && x <= halfW) {
            if (y >= 0 && y < halfH) {
                angle = 90 + angle;
            } else if (y >= halfH && y < height) {
                angle = 90 - angle;
            }
        } else if (x > halfW && x <= width) {
            if (y >= 0 && y < halfH) {
                angle = 270 - angle;
            } else if (y >= halfH && y < height) {
                angle = 270 + angle;
            }
        }
        int real = startAngle - DEFAULT_START_ANGLE;
        if (angle > real + sweepAngle || angle < real) {
            return;
        }
        int p = (int) (MAX_PROGRESS * (angle - real) / sweepAngle);
        debug(p);
        setProgress(p);
    }

    private int calDegree() {
        return (int) (sweepAngle / MAX_PROGRESS * this.progress);
    }

    private int dp2px(float d) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (d * scale + 0.5f);
    }
}
