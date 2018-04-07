package com.min.hybrid.library;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.min.fit.weex.R;

public class HandleResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_result);
        Intent intent = new Intent();
        intent.putExtra("name", "minych");
        setResult(1000, intent);
        this.finish();
    }
}
