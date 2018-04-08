package com.fit.we.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * 自定义ImageView, 点击时半透明
 */
@SuppressLint("AppCompatCustomView")
public class AlphaImageView extends ImageView {

    public AlphaImageView(Context context, AttributeSet attrs) {
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
