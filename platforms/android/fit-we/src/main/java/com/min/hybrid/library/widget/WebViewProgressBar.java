package com.min.hybrid.library.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.min.fit.weex.R;

public class WebViewProgressBar extends View {

    private Context mContext;
    private int mMax;
    private int mProgressHeight;
    private int mProgress;
    private int mWidth;
    private Paint mPaint;
    private int mColor;
    private ValueAnimator mAnimator;

    public WebViewProgressBar(Context context) {
        this(context, null);
    }

    public WebViewProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WebViewProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttr(attrs);
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.WebViewProgressBar);
        mMax = array.getInt(R.styleable.WebViewProgressBar_max, 100);
        mProgress = array.getInt(R.styleable.WebViewProgressBar_progress, 0);
        mProgressHeight = array.getDimensionPixelSize(R.styleable.WebViewProgressBar_progress_height, 16);
        mColor = array.getColor(R.styleable.WebViewProgressBar_progress_color, Color.parseColor("#0AC416"));
        array.recycle();

        mPaint = new Paint();
        mPaint.setColor(mColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float result = mWidth * ((float) mProgress / (float) mMax);
        canvas.drawRect(0, 0, result, mProgressHeight, mPaint);
    }

    public void setProgress(int newProgress) {
        mProgress = newProgress;
        postInvalidate();
    }

    public void setProgress(int distProgress, long time, final OnEndListener listener) {
        if (mProgress == 100) {
            mProgress = 0;
        }
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        mAnimator = ValueAnimator.ofInt(mProgress, distProgress);
        mAnimator.setDuration(time);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mProgress = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null) {
                    listener.onEnd();
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        mAnimator.start();

    }

    @Override
    protected void onDetachedFromWindow() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        super.onDetachedFromWindow();
    }

    public interface OnEndListener {
        void onEnd();
    }

}
