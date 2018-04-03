package com.weex.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.min.hybrid.library.container.FitContainerActivity;

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
