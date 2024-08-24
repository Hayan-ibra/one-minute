package com.example.oneminute.tourism.my_image_view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class DynamicImageView extends AppCompatImageView {

    public DynamicImageView(Context context) {
        super(context);
    }

    public DynamicImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Get the measured width of the ImageView
        int width = getMeasuredWidth();

        // Calculate the height based on the width
        int height = (int) (width * 0.75);

        // Set the calculated dimensions
        setMeasuredDimension(width, height);
    }
}