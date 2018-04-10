package com.fit.we.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by minyangcheng on 2018/4/5.
 */
public class LocalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);
        TextView tv = (TextView) findViewById(R.id.tv_info);
        String name = getIntent().getStringExtra("name");
        int age = getIntent().getIntExtra("age", -1);
        tv.setText("name=" + name + "\nage=" + age);
    }
}
