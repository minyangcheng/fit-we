package com.min.hybrid.library.container;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.min.hybrid.library.FitConstants;
import com.min.hybrid.library.bean.RouteInfo;

public class FitContainerActivity extends AppCompatActivity {

    FitContainerFragment mFitContainerFragment;

    public static void startActivity(Context context, RouteInfo routeInfo) {
        Intent intent = new Intent(context, FitContainerActivity.class);
        intent.putExtra(FitConstants.KEY_ROUTE_INFO, routeInfo);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String url) {
        RouteInfo routeInfo = new RouteInfo(url);
        startActivity(context, routeInfo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RouteInfo routeInfo = null;
        if (getIntent().hasExtra(FitConstants.KEY_ROUTE_INFO)) {
            routeInfo = (RouteInfo) getIntent().getSerializableExtra(FitConstants.KEY_ROUTE_INFO);
        }
        mFitContainerFragment = FitContainerFragment.newInstance(routeInfo);
        getSupportFragmentManager().beginTransaction()
            .replace(android.R.id.content, mFitContainerFragment)
            .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mFitContainerFragment != null) {
            mFitContainerFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        if (mFitContainerFragment != null) {
            mFitContainerFragment.onBackPressed();
        }
    }

    public FitContainerFragment getContainerFragment() {
        return mFitContainerFragment;
    }

}
