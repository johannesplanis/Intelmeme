package com.planis.johannes.intelmeme.utils;

import android.util.Log;

import com.planis.johannes.intelmeme.Constants;


/**
 * Created by pliszka on 09.10.15.
 */
public class L {
    private static final String TAG = "PLANIS_LABS";

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
