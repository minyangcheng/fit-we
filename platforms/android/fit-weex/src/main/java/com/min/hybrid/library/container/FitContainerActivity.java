package com.min.hybrid.library.container;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class FitContainerActivity extends AppCompatActivity {

    private String mBundleUrl = "http://10.10.12.151:8888/page/bundle.js?routeInfo={path:'MainPage',param:{name:'minyangcheng',age:11}}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FitContainerFragment fitContainerFragment = FitContainerFragment.newInstance(mBundleUrl);
        getFragmentManager().beginTransaction()
            .replace(android.R.id.content, fitContainerFragment)
            .commit();
    }

}
