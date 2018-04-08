package com.fit.we.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * 自定义TextView, 点击时半透明
 */
@SuppressLint("AppCompatCustomView")
public class AlphaTextView extends TextView {

    public AlphaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            setAlpha(0.6f);
        } else {
            setAlpha(1.0f);
        }
        return super.onTouchEvent(event);
    }
}
