package com.fit.we.library.extend.module;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baoyz.actionsheet.ActionSheet;
import com.fit.we.library.R;
import com.fit.we.library.extend.weex.IWeexHandler;
import com.fit.we.library.extend.weex.WeexHandlerManager;
import com.fit.we.library.util.DialogUtil;
import com.fit.we.library.widget.popmenu.FrmPopMenu;
import com.fit.we.library.widget.popmenu.PopClickListener;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

;

public class UiModule extends WXModule {

    /**
     * 消息提示
     * message： 需要提示的消息内容
     * duration：显示时长,long或short
     */
    @JSMethod(uiThread = true)
    public void toast(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        Context context = mWXSDKInstance.getContext();
        String message = params.getString("message");
        String duration = params.getString("duration");
        if (!TextUtils.isEmpty(message)) {
            if ("long".equalsIgnoreCase(duration)) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        }
        successCallback.invoke(null);
    }

    /**
     * 弹出确认对话框
     * title：标题
     * message：消息
     * cancelable：是否可取消
     * buttonLabels：按钮数组，最多设置2个按钮
     * 返回：
     * which：按钮id
     */
    @JSMethod(uiThread = true)
    public void alert(JSONObject params, final JSCallback successCallback, JSCallback errorCallback) {
        Context context = mWXSDKInstance.getContext();
        String title = params.getString("title");
        String message = params.getString("message");
        JSONArray jsonArray = params.getJSONArray("buttonLabels");
        String btnName_1 = null;
        String btnName_2 = null;
        if (jsonArray != null && jsonArray.size() > 0) {
            if (jsonArray.size() == 1) {
                btnName_1 = jsonArray.getString(0);
            } else {
                btnName_1 = jsonArray.getString(0);
                btnName_2 = jsonArray.getString(1);
            }
        } else {
            btnName_1 = "确定";
        }
        boolean cancelable = !"0".equals(params.getString("cancelable"));
        DialogUtil.showAlertDialog(context, title, message, btnName_1, btnName_2, cancelable, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                JSONObject data = new JSONObject();
                data.put("which", 0);
                successCallback.invoke(data);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                JSONObject data = new JSONObject();
                data.put("which", 1);
                successCallback.invoke(data);
            }
        });
    }

    /**
     * 弹出日期选择对话框
     * 参数：
     * title： 标题
     * datetime： 指定日期 yyyy-MM-dd
     * date： 格式：yyyy-MM-dd
     */
    @JSMethod(uiThread = true)
    public void pickDate(JSONObject params, final JSCallback successCallback, JSCallback errorCallback) {
        Context context = mWXSDKInstance.getContext();
        String title = params.getString("title");
        String dateFormatStr = params.getString("dateFormat");
        String date = params.getString("datetime");
        DialogUtil.showDateDialog(context, title, dateFormatStr, date, true, new DialogUtil.OnDateDialogListener() {
            @Override
            public void onDataSelect(String dateStr) {
                JSONObject result = new JSONObject();
                result.put("date", dateStr);
                successCallback.invoke(result);
            }
        });
    }

    /**
     * 弹出年月选择对话框
     * title： 标题
     * datetime： 指定日期 yyyy-MM
     * 返回：
     * month： 格式：yyyy-MM
     */
    @JSMethod(uiThread = true)
    public void pickMonth(JSONObject params, final JSCallback successCallback, JSCallback errorCallback) {
        Context context = mWXSDKInstance.getContext();
        String title = params.getString("title");
        String dateFormatStr = params.getString("dateFormat");
        String date = params.getString("datetime");
        DialogUtil.showDateDialog(context, title, dateFormatStr, date, false, new DialogUtil.OnDateDialogListener() {
            @Override
            public void onDataSelect(String dateStr) {
                JSONObject result = new JSONObject();
                result.put("date", dateStr);
                successCallback.invoke(result);
            }
        });
    }

    /**
     * 弹出时间选择对话框
     * 参数：
     * title：标题
     * datetime 指定时间 yyyy-MM-dd HH:mm或者HH:mm
     * 返回：
     * time：格式：HH:mm
     */
    @JSMethod(uiThread = true)
    public void pickTime(JSONObject params, final JSCallback successCallback, JSCallback errorCallback) {
        Context context = mWXSDKInstance.getContext();
        String title = params.getString("title");
        String timeFormat = params.getString("timeFormat");
        final String time = params.getString("time");
        DialogUtil.showTimeDialog(context, title, timeFormat, time, new DialogUtil.OnTimeDialogListener() {
            @Override
            public void onTimeSelect(String timeStr) {
                JSONObject result = new JSONObject();
                result.put("time", timeStr);
                successCallback.invoke(result);
            }
        });
    }

    /**
     * 弹出底部选项按钮
     * items：多个选项用,隔开
     * cancelable: 是否可取消
     * 返回：
     * which：选中的按钮id
     */
    @JSMethod(uiThread = true)
    public void actionSheet(JSONObject params, final JSCallback successCallback, JSCallback errorCallback) {
        AppCompatActivity activity = (AppCompatActivity) mWXSDKInstance.getContext();
        boolean cancelable = !"0".equals(params.getString("cancelable"));
        String cancelBtnName = params.getString("cancelBtnName");
        JSONArray jsonArr = params.getJSONArray("items");
        if (jsonArr == null && jsonArr.size() == 0) {
            errorCallback.invoke(mWXSDKInstance.getContext().getString(R.string.status_request_error));
            return;
        }
        String[] items = new String[jsonArr.size()];
        items = jsonArr.toArray(items);
        ActionSheet.createBuilder(activity, activity.getSupportFragmentManager())
            .setCancelButtonTitle(cancelBtnName)
            .setOtherButtonTitles(items)
            .setCancelableOnTouchOutside(cancelable)
            .setListener(new ActionSheet.ActionSheetListener() {
                @Override
                public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                    JSONObject result = new JSONObject();
                    result.put("which", -1);
                    successCallback.invoke(result);
                }

                @Override
                public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                    JSONObject result = new JSONObject();
                    result.put("which", index);
                    successCallback.invoke(result);
                }
            });
    }

    /**
     * 弹出顶部选项按钮
     * iconFilterColor：图标过滤色
     * titleItems：多个选项用,隔开
     * iconItems: 图标 多个,隔开
     * which：点击按钮id
     */
    @JSMethod(uiThread = true)
    public void popWindow(JSONObject params, final JSCallback successCallback, JSCallback errorCallback) {
        IWeexHandler weexHandler = WeexHandlerManager.getWeexHandler(mWXSDKInstance);
        if (weexHandler != null) {
            String iconFilterColor = params.getString("iconFilterColor");
            JSONArray titleJsonObject = params.getJSONArray("titleItems");
            JSONArray iconJsonObject = params.getJSONArray("iconItems");
            if (titleJsonObject == null) {
                errorCallback.invoke(mWXSDKInstance.getContext().getString(R.string.status_request_error));
                return;
            }
            String[] titleItems = new String[titleJsonObject.size()];
            String[] iconItems = new String[iconJsonObject.size()];
            titleItems = titleJsonObject.toArray(titleItems);
            iconItems = iconJsonObject.toArray(iconItems);
            if (iconItems != null && titleItems.length != iconItems.length) {
                errorCallback.invoke(mWXSDKInstance.getContext().getString(R.string.status_request_error));
                return;
            }

            int iconColor = 0;
            if (!TextUtils.isEmpty(iconFilterColor)) {
                iconColor = Color.parseColor("#" + iconFilterColor);
            }
            FrmPopMenu popupWindow = new FrmPopMenu(mWXSDKInstance.getContext(), weexHandler.getNBRoot(), titleItems, iconItems, new PopClickListener() {
                @Override
                public void onClick(int index) {
                    JSONObject data = new JSONObject();
                    data.put("which", index);
                    successCallback.invoke(data);
                }
            });
            popupWindow.setIconFilterColor(iconColor);
            popupWindow.show();
        }
    }

    /**
     * 显示loading
     */
    @JSMethod(uiThread = true)
    public void showLoadingDialog(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        IWeexHandler weexHandler = WeexHandlerManager.getWeexHandler(mWXSDKInstance);
        if (weexHandler != null) {
            String message = params.getString("message");
            boolean cancelable = true;
            if (params.containsKey("cancelable")) {
                cancelable = !"0".equals(params.getString("cancelable"));
            }
            weexHandler.showHudDialog(message, cancelable);
            successCallback.invoke(null);
        }
    }

    /**
     * 隐藏loading
     */
    @JSMethod(uiThread = true)
    public void closeLoadingDialog(JSONObject params, JSCallback successCallback, JSCallback errorCallback) {
        IWeexHandler weexHandler = WeexHandlerManager.getWeexHandler(mWXSDKInstance);
        if (weexHandler != null) {
            weexHandler.hideHudDialog();
            successCallback.invoke(null);
        }
    }

}
