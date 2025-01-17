package com.example.weso_p01;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.VideoView;

public class CircularVideoView extends VideoView {
    private Path clipPath;
    private Paint borderPaint;
    private float borderWidth = 10f; // Set the border width here
    private int desiredSize = 180; // Default size in dp

    public CircularVideoView(Context context) {
        super(context);
        init();
    }

    public CircularVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircularVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        clipPath = new Path();

        // Initialize the border paint
        borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(0xFFBA68C8); // Set the border color here
        borderPaint.setStrokeWidth(borderWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Enforce dynamic size based on the desiredSize value
        int sizeInPx = dpToPx(desiredSize); // Convert dp to pixels
        setMeasuredDimension(sizeInPx, sizeInPx);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateClipPath(w, h);
    }

    private void updateClipPath(int w, int h) {
        clipPath.reset();
        clipPath.addCircle(w / 2f, h / 2f, Math.min(w, h) / 2f, Path.Direction.CW);
        clipPath.close();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(clipPath);
        super.dispatchDraw(canvas);
        canvas.restore();

        // Draw the border
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, (Math.min(getWidth(), getHeight()) / 2f) - borderWidth / 2, borderPaint);
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    // Method to dynamically change the size of the CircularVideoView
    public void setDesiredSize(int sizeInDp) {
        this.desiredSize = sizeInDp;

        // Update the layout parameters to apply the new size
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = dpToPx(desiredSize);
        params.height = dpToPx(desiredSize);
        setLayoutParams(params);

        // Request a layout pass to apply the changes
        requestLayout();
    }
}
