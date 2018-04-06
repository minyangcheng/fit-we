package com.min.hybrid.library;

/**
 * Created by minyangcheng on 2018/1/10.
 */

public class FitConstants {

    public static final String LOG_TAG = "FIT_WE";

    public static final String FIT_SCHEME = "fit";

    public static final String KEY_ROUTE_INFO = "routeInfo";

    public static final String REQUEST_CODE = "requestCode";
    public static final String RESULT_DATA = "resultData";


    public static class Resource {
        public static final String BASE_DIR = "WebApp";
        public static final String BUNDLE_NAME = "bundle.zip";
        public static final String TEMP_BUNDLE_NAME = "temp_bundle.zip";
        public static final String JS_BUNDLE = "/.bundle";
        public static final String JS_BUNDLE_ZIP = "/.bundle_zip";
    }

    public static class Version {
        public static final int UPDATING = 0;
        public static final int SLEEP = 1;
    }

    public static class SP {
        public static final String NATIVE_NAME = "HYBIRD_NATIVE_SP";
        public static final String VERSION = "VERSION";
        public static final String DOWNLOAD_VERSION = "DOWNLOAD_VERSION";
        public static final String INTERCEPTOR_ACTIVE = "INTERCEPTOR_ACTIVE";
        public static final String PAGE_DEV_HOST_URL = "PAGE_DEV_HOST_URL";
    }

}
