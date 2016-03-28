package com.planis.johannes.intelmeme;

import android.util.Log;


/**
 * Created by pliszka on 09.10.15.
 */
public class L {
    public static final String TAG = "REDMOBILE";

    public static void i(String str) {
        if (Constants.DEBUG_LOG) {
            Log.i(TAG, str);
        }
    }

    public static void w(String str) {
        if (Constants.DEBUG_LOG) {
            Log.w(TAG, str);
        }
    }

    public static void d(String str) {
        if (Constants.DEBUG_LOG) {
            Log.d(TAG, str);
        }
    }

    public static void e(String str) {
        if (Constants.DEBUG_LOG) {
            Log.e(TAG, str);
        }
    }

    public static void e(String str, Exception e) {
        if (Constants.DEBUG_LOG) {
            if (e != null) {
                Log.e(TAG, str + " Exception: ", e);
            }
        }
    }
}
