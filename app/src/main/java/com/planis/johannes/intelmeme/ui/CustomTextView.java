package com.planis.johannes.intelmeme.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by JOHANNES on 3/31/2016.
 */
public class CustomTextView extends TextView {
    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/Komix.ttf"));
    }
}
