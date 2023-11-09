package com.example.to_do;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PageView extends View {
    private Paint yellowPaint;
    private Paint darkYellowPaint;

    public PageView(Context context) {
        super(context);
        init();
    }

    public PageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        yellowPaint = new Paint();
        yellowPaint.setColor(0xFFFFC0CB);  // Yellow color
        yellowPaint.setStyle(Paint.Style.FILL);

        darkYellowPaint = new Paint();
        darkYellowPaint.setColor(0xFFA9A9A9);  // Dark Yellow color
        darkYellowPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        // Draw the slim yellow rectangle
        RectF rect = new RectF(0, 0, width, height);
        canvas.drawRect(rect, yellowPaint);

        // Draw the inward dark yellow triangle
        int triangleSize = Math.min(width, height) / 10; // Size of the small brown triangle

        // Draw the slim yellow rectangle
        canvas.drawRect(0, 0, width, height, yellowPaint);

        // Draw the small inward-facing brown triangle in the bottom-right corner
        Path trianglePath = new Path();
        trianglePath.moveTo(0, 0);
        trianglePath.lineTo(0, triangleSize);
        trianglePath.lineTo(triangleSize, 0);
        trianglePath.close();

        canvas.drawPath(trianglePath, darkYellowPaint);

      //  canvas.drawPath(trianglePath, darkYellowPaint);
    }
}

