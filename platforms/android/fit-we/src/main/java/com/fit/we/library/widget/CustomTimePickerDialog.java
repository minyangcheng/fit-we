package com.fit.we.library.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.fit.we.library.R;;

/**
 * Created by minych on 18-2-28.
 */

public class CustomTimePickerDialog extends AlertDialog implements DialogInterface.OnClickListener, TimePicker.OnTimeChangedListener {

    private static final String START_HOUR = "hour";
    private static final String START_MINUTE = "minute";

    private int hour;
    private int minute;

    private TimePicker.OnTimeChangedListener listener;
    private TimePicker timePicker;

    public CustomTimePickerDialog(Context context, int hour, int minute, TimePicker.OnTimeChangedListener listener) {
        super(context);
        this.hour = hour;
        this.minute = minute;
        this.listener = listener;
        init();
    }

    private void init() {
        this.setTitle("请选择时间");
        setButton(BUTTON_POSITIVE, "确定", this);
        setButton(BUTTON_NEGATIVE, "取消", this);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_date_picker, null);
        setView(view);
        view.findViewById(R.id.datePicker).setVisibility(View.GONE);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
        timePicker.setOnTimeChangedListener(this);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == BUTTON_POSITIVE) {
            listener.onTimeChanged(timePicker, hour, minute);
        }
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(START_HOUR, hour);
        state.putInt(START_MINUTE, minute);
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        hour = savedInstanceState.getInt(START_HOUR);
        minute = savedInstanceState.getInt(START_MINUTE);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
    }

}
