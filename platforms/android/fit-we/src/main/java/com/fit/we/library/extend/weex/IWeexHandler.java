package com.fit.we.library.extend.weex;

import android.view.View;

/**
 * Created by minych on 18-11-9.
 */

public interface IWeexHandler {

    LongCallbackHandler getLongCallbackHandler();

    void refresh();

    void showHudDialog(String message, boolean cancelable);

    void hideHudDialog();

    void setNBVisibility(boolean visible);

    void setNBBackBtnVisibility(boolean visible);

    void setNBTitle(String title, String subTitle);

    void setNBTitleClickable(boolean clickable, int arrow);

    void setNBLeftBtn(String imageUrl, String text);

    void hideNBLeftBtn();

    void setNBRightBtn(int which, String imageUrl, String text);

    void hideNBRightBtn(int which);

    View getNBRoot();
}
