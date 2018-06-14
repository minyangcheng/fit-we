package com.fit.we.library.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fit.we.library.R;;
import com.fit.we.library.util.UriHandler;


/**
 * Created by dailichun on 2017/12/8.
 * 框架默认导航栏控制器，对应布局文件R.layout.frm_nb_style1。需要传递参数nbStyle=1。
 */
public class NavigationBar extends FrameLayout implements View.OnClickListener {

    //最左侧图标按钮，一般是返回按钮
    public AlphaImageView nbBackIv1;

    //最左侧图文按钮
    public DrawableText nbBackTv1;

    //左侧图标按钮，在nbBack右侧
    public AlphaImageView nbLeftIv2;

    //左侧文字按钮，在nbBack右侧
    public AlphaTextView nbLeftTv2;

    //右侧图标按钮,框架默认4个
    public AlphaImageView[] nbRightIvs;

    //最右侧文字按钮,框架默认2个
    public AlphaTextView[] nbRightTvs;

    //标题父控件
    public View titleParent;

    //主标题
    public TextView nbTitle;

    //副标题
    public TextView nbTitle2;

    //标题箭头，可点击时展示
    public ImageView ivTitleArrow;

    //自定义标题父控件
    public FrameLayout nbCustomTitleLayout;

    //线
    public LinearLayout line;

    //导航栏根布局
    public View nbRoot;

    public INbOnClick clickListener;

    public NavigationBar(@NonNull Context context) {
        super(context);
        init();
    }

    public NavigationBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NavigationBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.frm_nb_style1, this);
        findView();
    }

    private void findView() {
        nbRoot = findViewById(R.id.nbRoot);
        line = (LinearLayout) findViewById(R.id.line);

        nbBackIv1 = (AlphaImageView) findViewById(R.id.nbLeftIv1);
        nbBackIv1.setOnClickListener(this);
        nbBackTv1 = (DrawableText) findViewById(R.id.nbLeftTv1);
        nbBackTv1.setOnClickListener(this);
        nbLeftTv2 = (AlphaTextView) findViewById(R.id.nbLeftTv2);
        nbLeftTv2.setOnClickListener(this);
        nbLeftIv2 = (AlphaImageView) findViewById(R.id.nbLeftIv2);
        nbLeftIv2.setOnClickListener(this);

        nbRightIvs = new AlphaImageView[4];
        nbRightIvs[0] = (AlphaImageView) findViewById(R.id.nbRightIv1);
        nbRightIvs[0].setOnClickListener(this);
        nbRightIvs[1] = (AlphaImageView) findViewById(R.id.nbRightIv2);
        nbRightIvs[1].setOnClickListener(this);
        nbRightIvs[2] = (AlphaImageView) findViewById(R.id.nbRightIv3);
        nbRightIvs[2].setOnClickListener(this);
        nbRightIvs[3] = (AlphaImageView) findViewById(R.id.nbRightIv4);
        nbRightIvs[3].setOnClickListener(this);

        nbRightTvs = new AlphaTextView[2];
        nbRightTvs[0] = (AlphaTextView) findViewById(R.id.nbRightTv1);
        nbRightTvs[0].setOnClickListener(this);
        nbRightTvs[1] = (AlphaTextView) findViewById(R.id.nbRightTv2);
        nbRightTvs[1].setOnClickListener(this);

        titleParent = findViewById(R.id.rl_title);
        titleParent.setClickable(false);
        titleParent.setOnClickListener(this);
        nbTitle = (TextView) findViewById(R.id.nbTitle);
        nbTitle2 = (TextView) findViewById(R.id.nbTitle2);
        ivTitleArrow = (ImageView) findViewById(R.id.iv_arrow);

        nbCustomTitleLayout = (FrameLayout) findViewById(R.id.nbCustomTitleLayout);
    }

    public View getNavigationView() {
        return nbRoot;
    }

    public void hide() {
        this.setVisibility(View.GONE);
    }

    public void show() {
        this.setVisibility(View.VISIBLE);
    }

    public void hideLine() {
        if (line != null) {
            line.setVisibility(View.GONE);
        }
    }

    public void hideNbBack() {
        if (nbBackIv1 != null) {
            nbBackIv1.setVisibility(View.GONE);
        }
    }

    public void showNbBack() {
        if (nbBackIv1 != null) {
            nbBackIv1.setVisibility(View.VISIBLE);
        }
    }

    public void addNbCustomTitleView(View view) {
        if (titleParent == null || nbCustomTitleLayout == null) {
            return;
        }
        titleParent.setVisibility(View.GONE);
        nbCustomTitleLayout.setVisibility(View.VISIBLE);
        nbCustomTitleLayout.removeAllViews();
        nbCustomTitleLayout.addView(view);
    }

    public void setTitleClickable(boolean clickable, int arrow) {
        if (titleParent == null || ivTitleArrow == null) {
            return;
        }
        titleParent.setClickable(clickable);
        if (clickable) {
            ivTitleArrow.setImageResource(arrow);
            ivTitleArrow.setVisibility(View.VISIBLE);
        } else {
            ivTitleArrow.setVisibility(View.GONE);
        }
    }

    public void setNbBackImage(Object bg) {
        if (nbBackIv1 != null) {
            if (bg instanceof Integer) {
                nbBackIv1.setImageResource((int) bg);
            } else if (bg instanceof String) {
                UriHandler.displayImage(nbLeftIv2, (String) bg);
            }
        }
    }

    public void setColorFilter(Object filterColor) {
        int filterColorId = Color.BLACK;
        if (filterColor instanceof Integer) {
            int themeColor = (int) filterColor;
            if (themeColor <= 0) {
                filterColorId = themeColor;
            } else {
                filterColorId = getContext().getResources().getColor(themeColor);
            }
        } else if (filterColor instanceof String) {
            String themeColor = (String) filterColor;
            filterColorId = Color.parseColor(themeColor);
        }
        if (nbRightTvs != null) {
            for (AlphaTextView tv : nbRightTvs) {
                if (tv != null) {
                    tv.setTextColor(filterColorId);
                }
            }
        }
        if (nbRightIvs != null) {
            for (AlphaImageView iv : nbRightIvs) {
                if (iv != null) {
                    iv.setColorFilter(filterColorId);
                }
            }
        }
        if (nbBackIv1 != null) {
            nbBackIv1.setColorFilter(filterColorId);
        }
        if (nbBackTv1 != null) {
            nbBackTv1.setTextColor(filterColorId);
        }
        if (nbLeftIv2 != null) {
            nbLeftIv2.setColorFilter(filterColorId);
        }
        if (nbLeftTv2 != null) {
            nbLeftTv2.setTextColor(filterColorId);
        }
    }

    public void setNbTitle(String title) {
        if (nbTitle != null) {
            nbTitle.setText(title);
        }
    }

    public void setNbTitle(String title, String title2) {
        setNbTitle(title);
        if (nbTitle2 != null) {
            if (TextUtils.isEmpty(title2)) {
                nbTitle2.setVisibility(View.GONE);
            } else {
                nbTitle2.setText(title2);
                nbTitle2.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setLeftBtn(String imageUrl, String text) {
        if (!TextUtils.isEmpty(imageUrl)) {
            nbLeftTv2.setVisibility(View.GONE);
            nbLeftIv2.setVisibility(View.VISIBLE);
            UriHandler.displayImage(nbLeftIv2, imageUrl);
        } else {
            nbLeftIv2.setVisibility(View.GONE);
            nbLeftTv2.setVisibility(View.VISIBLE);
            nbLeftTv2.setText(text);
        }
    }

    public void hideLeftBtn() {
        nbLeftTv2.setVisibility(View.GONE);
        nbLeftIv2.setVisibility(View.GONE);
    }

    public void setRightBtn(int which, String imageUrl, String text) {
        if (!TextUtils.isEmpty(imageUrl)) {
            if (which < nbRightTvs.length) nbRightTvs[which].setVisibility(View.INVISIBLE);
            if (which < nbRightIvs.length) nbRightIvs[which].setVisibility(View.VISIBLE);
            if (which < nbRightIvs.length)
                UriHandler.displayImage(nbRightIvs[which], imageUrl);
        } else {
            if (which < nbRightIvs.length) nbRightIvs[which].setVisibility(View.INVISIBLE);
            if (which < nbRightTvs.length) nbRightTvs[which].setVisibility(View.VISIBLE);
            if (which < nbRightTvs.length) nbRightTvs[which].setText(text);
        }
    }

    public void hideRightBtn(int which) {
        if (which < nbRightTvs.length) nbRightTvs[which].setVisibility(View.INVISIBLE);
        if (which < nbRightIvs.length) nbRightIvs[which].setVisibility(View.INVISIBLE);
    }

    public void onClick(View v) {
        if (clickListener != null) {
            if (nbRightTvs != null) {
                for (int i = 0; i < nbRightTvs.length; i++) {
                    if (v == nbRightTvs[i]) {
                        clickListener.onNbRight(v, i);
                        return;
                    }
                }
            }
            if (nbRightIvs != null) {
                for (int i = 0; i < nbRightIvs.length; i++) {
                    if (v == nbRightIvs[i]) {
                        clickListener.onNbRight(v, i);
                        return;
                    }
                }
            }
            if (v == titleParent) {
                clickListener.onNbTitle(v);
                return;
            }
            if (v == nbBackIv1 || v == nbBackTv1) {
                clickListener.onNbBack();
                return;
            }
            if (v == nbLeftIv2 || v == nbLeftTv2) {
                clickListener.onNbLeft(v);
            }
        }
    }

    public void setOnNavigationBarListener(INbOnClick listener) {
        this.clickListener = listener;
    }

    public interface INbOnClick {

        void onNbBack();

        void onNbLeft(View view);

        void onNbRight(View view, int which);

        void onNbTitle(View view);

    }

}
