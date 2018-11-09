package com.fit.we.library.extend.compontent;

import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.AbstractEditComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.WXEditText;

/**
 * Created by minyangcheng on 2018/6/3.
 */
@Component(lazyload = false)
public class InputEditText extends AbstractEditComponent {

    private static final String TAG = InputEditText.class.getSimpleName();

    public InputEditText(WXSDKInstance instance, WXDomObject dom, WXVContainer parent, boolean isLazy) {
        super(instance, dom, parent, isLazy);
    }


    @JSMethod
    public void clear() {
        WXEditText host = getHostView();
        if (host != null && host.getText().length() > 0) {
            host.getText().clear();
        }
    }

    @JSMethod
    public void show(boolean isShowPassWord) {
        WXEditText editText = getHostView();
        if (editText != null) {
            if (isShowPassWord) {
                editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    @JSMethod
    public void setInputText(String s) {
        WXEditText editText = getHostView();
        if (editText != null) {
            editText.setText(s);
        }
    }

    @WXComponentProp(name = "keyType")
    public void setKeyType(String type) {
        WXEditText editText = getHostView();
        if (editText != null) {
            editText.setInputType(getInputType(type));
        }
    }

    @WXComponentProp(name = "decimalLen")
    public void setDecimalLen(int len) {
        WXEditText editText = getHostView();
        if (editText != null) {
            this.setKeyType("decimal");
            editText.setFilters(new InputFilter[]{new DecimalFilter().setDigits(len)});
        }
    }

    private int getInputType(String type) {
        int inputType;
        switch (type) {
            case Constants.Value.TEXT:
                inputType = InputType.TYPE_CLASS_TEXT;
                break;
            case Constants.Value.EMAIL:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
                break;
            case Constants.Value.PASSWORD:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                getHostView().setTransformationMethod(PasswordTransformationMethod.getInstance());
                break;
            case "phone":
                inputType = InputType.TYPE_CLASS_PHONE;
                break;
            case Constants.Value.NUMBER:
                inputType = InputType.TYPE_CLASS_NUMBER;
                break;
            case "decimal":
                inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
                break;
            default:
                inputType = InputType.TYPE_CLASS_TEXT;
        }
        return inputType;
    }

    public class DecimalFilter extends DigitsKeyListener {

        private int digits = 2;

        public DecimalFilter() {
            super(false, true);
        }

        public DecimalFilter setDigits(int d) {
            digits = d;
            return this;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            CharSequence out = super.filter(source, start, end, dest, dstart, dend);
            // if changed, replace the source
            if (out != null) {
                source = out;
                start = 0;
                end = out.length();
            }

            int len = end - start;

            // if deleting, source is empty
            // and deleting can't break anything
            if (len == 0) {
                return source;
            }

            //以点开始的时候，自动在前面添加0
            if (source.toString().equals(".") && dstart == 0) {
                return "0.";
            }
            //如果起始位置为0,且第二位跟的不是".",则无法后续输入
            if (!source.toString().equals(".") && dest.toString().equals("0")) {
                return "";
            }

            int dlen = dest.length();

            // Find the position of the decimal .
            for (int i = 0; i < dstart; i++) {
                if (dest.charAt(i) == '.') {
                    // being here means, that a number has
                    // been inserted after the dot
                    // check if the amount of digits is right
                    return (dlen - (i + 1) + len > digits) ?
                        "" :
                        new SpannableStringBuilder(source, start, end);
                }
            }

            for (int i = start; i < end; ++i) {
                if (source.charAt(i) == '.') {
                    // being here means, dot has been inserted
                    // check if the amount of digits is right
                    if ((dlen - dend) + (end - (i + 1)) > digits)
                        return "";
                    else
                        break;  // return new SpannableStringBuilder(source, start, end);
                }
            }


            // if the dot is after the inserted part,
            // nothing can break
            return new SpannableStringBuilder(source, start, end);
        }
    }

}
