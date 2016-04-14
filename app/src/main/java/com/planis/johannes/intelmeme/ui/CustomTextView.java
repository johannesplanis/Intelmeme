package com.planis.johannes.intelmeme.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.planis.johannes.intelmeme.R;

/**
 * Created by JOHANNES on 3/31/2016.
 */
public class CustomTextView extends TextView {
    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/impact.ttf"));
    }


    private static final int BORDER_WIDTH = 5;


    @Override
    public void draw(Canvas canvas) {
        int originalColor = this.getCurrentTextColor();
        this.setTextColor(0xff000000); //set it to white.

        canvas.saveLayer(null, new Paint(R.color.black), Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
                | Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.MATRIX_SAVE_FLAG);

        drawBackground(canvas, -BORDER_WIDTH, -BORDER_WIDTH);
        drawBackground(canvas, BORDER_WIDTH + BORDER_WIDTH, 0);
        drawBackground(canvas, 0, BORDER_WIDTH + BORDER_WIDTH);
        drawBackground(canvas, -BORDER_WIDTH - BORDER_WIDTH, 0);

        this.setTextColor(originalColor);
        canvas.restore();
        super.draw(canvas);
    }

    private void drawBackground(Canvas aCanvas, int aDX, int aDY) {
        aCanvas.translate(aDX, aDY);
        super.draw(aCanvas);
    }
}
