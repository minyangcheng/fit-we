package com.fit.we.library.util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.fit.we.library.widget.CustomDatePickerDialog;
import com.fit.we.library.widget.CustomTimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by minych on 18-2-27.
 */

public class DialogUtil {

    public static void showAlertDialog(Context context, String title, String message, String btnName_1, String btnName_2, boolean cancelable, DialogInterface.OnClickListener btnListener_1, DialogInterface.OnClickListener btnListener_2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        if (!TextUtils.isEmpty(btnName_1)) {
            builder.setNegativeButton(btnName_1, btnListener_1);
        }
        if (!TextUtils.isEmpty(btnName_2)) {
            builder.setPositiveButton(btnName_2, btnListener_2);
        }
        builder.setCancelable(cancelable);
        builder.show();
    }

    public static void showDateDialog(Context context, String title, String dateFormatStr, String dateStr, boolean isShowDay, final OnDateDialogListener listener) {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat(dateFormatStr);
        if (!TextUtils.isEmpty(dateStr)) {
            try {
                Date date = sdf.parse(dateStr);
                calendar.setTime(date);
            } catch (Exception e) {
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        CustomDatePickerDialog datePickerDialog = new CustomDatePickerDialog(context, isShowDay, year, month, day, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                if (listener != null) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    listener.onDataSelect(sdf.format(calendar.getTime()));
                }
            }
        });
        datePickerDialog.setTitle(title);
        datePickerDialog.show();
    }

    public static void showTimeDialog(Context context, String title, String timeFormatStr, String timeStr, final OnTimeDialogListener listener) {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat(timeFormatStr);
        if (!TextUtils.isEmpty(timeStr)) {
            try {
                Date date = sdf.parse(timeStr);
                calendar.setTime(date);
            } catch (Exception e) {
            }
        }
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        CustomTimePickerDialog dialog = new CustomTimePickerDialog(context, hour, minute, new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                if (listener != null) {
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    listener.onTimeSelect(sdf.format(calendar.getTime()));
                }
            }
        });
        dialog.setTitle(title);
        dialog.show();
    }

    public interface OnDateDialogListener {
        void onDataSelect(String dateStr);
    }

    public interface OnTimeDialogListener {
        void onTimeSelect(String timeStr);
    }

}
