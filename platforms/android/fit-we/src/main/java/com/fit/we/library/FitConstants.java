package com.fit.we.library;

/**
 * Created by minyangcheng on 2018/1/10.
 */

public class FitConstants {

    public static final String LOG_TAG = "FIT_WE";

    public static final String MODULE_FILE_SUFFIX = "_fit.json";

    public static final String KEY_ROUTE = "route";

    public static final String KEY_NATIVE_PARAMS = "nativeParams";

    public static class Resource {
        public static final String BASE_DIR = "FitApp";
        public static final String BUNDLE_NAME = "bundle.zip";
        public static final String TEMP_BUNDLE_NAME = "temp_bundle.zip";
        public static final String JS_BUNDLE = "/.bundle";
        public static final String JS_BUNDLE_ZIP = "/.bundle_zip";
        public static final String JS_CHECK_SIGNATURE = "/.check_signature";
    }

    public static class Version {
        public static final int UPDATING = 0;
        public static final int SLEEP = 1;
    }

    public static class SP {
        public static final String PREFERENCE_NAME = "fit_we";
        public static final String VERSION = "VERSION";
        public static final String DOWNLOAD_VERSION = "DOWNLOAD_VERSION";
        public static final String LOCAL_FILE_ACTIVE = "LOCAL_FILE_ACTIVE";
        public static final String FIT_WE_SERVER = "FIT_WE_SERVER";
    }

}
