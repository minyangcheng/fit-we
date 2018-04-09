package com.fit.we.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fit.we.library.container.FitContainerActivity;

/**
 * Created by minyangcheng on 2018/4/1.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickGo(View view) {
        Intent intent = new Intent(this, FitContainerActivity.class);
        startActivity(intent);
    }

}
