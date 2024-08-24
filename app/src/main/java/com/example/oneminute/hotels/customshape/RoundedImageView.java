package com.example.oneminute.hotels.customshape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

public class RoundedImageView extends androidx.appcompat.widget.AppCompatImageView {

    private Path path;
    private RectF rect;
    private float cornerRadius = 40.0f; // Adjust this value as needed

    public RoundedImageView(Context context) {
        super(context);
        init();
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        path.reset();
        path.addRoundRect(rect, new float[]{0, 0, 0, 0, 0, 0, cornerRadius, cornerRadius}, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}