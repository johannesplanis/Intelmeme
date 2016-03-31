package com.planis.johannes.intelmeme.utils;

import android.graphics.Bitmap;

/**
 * Created by JOHANNES on 3/29/2016.
 */
public class DataManager {

    Bitmap bitmap;

    Bitmap finishedMeme;

    public Bitmap getFinishedMeme() {
        return finishedMeme;
    }

    public void setFinishedMeme(Bitmap finishedMeme) {
        this.finishedMeme = finishedMeme;
    }

    public DataManager() {
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
