package com.fit.we.library.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.fit.we.library.R;;

public class HudDialog extends Dialog {

    private String mess;
    private TextView messTv;

    public static HudDialog createProgressHud(Context context) {
        return createProgressHud(context, null, true, null);
    }

    public static HudDialog createProgressHud(Context context, String mess, boolean canCancel, OnCancelListener onCancelListener) {
        HudDialog dialog = new HudDialog(context);
        dialog.setMessage(mess);
        dialog.setCancelable(canCancel);
        dialog.setCanceledOnTouchOutside(false);
        if (onCancelListener != null) {
            dialog.setOnCancelListener(onCancelListener);
        }
        return dialog;
    }

    public HudDialog(Context context) {
        super(context, R.style.HudStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress_hud);
        initView();

        getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        getWindow().setAttributes(lp);
    }

    private void initView() {
        messTv = (TextView) findViewById(R.id.mess_tv);
        setMessage(mess);
    }

    public void setMessage(String message) {
        this.mess = message;
        if (messTv != null) {
            messTv.setText(message);
            if(TextUtils.isEmpty(message)){
                messTv.setVisibility(View.GONE);
            }else{
                messTv.setVisibility(View.VISIBLE);
            }
        }
    }

}
