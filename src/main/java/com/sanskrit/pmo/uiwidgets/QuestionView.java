package com.sanskrit.pmo.uiwidgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.sanskrit.pmo.R;


public class QuestionView extends View {
    Paint paint;
    float progress;
    int strokeWidth = getDimensionInPixel(10);

    public QuestionView(Context context) {
        super(context);
        init();
    }

    public QuestionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QuestionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        this.paint = new Paint();
        this.paint.setColor(getResources().getColor(R.color.color_twitter));
        this.paint.setStrokeWidth((float) this.strokeWidth);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        if (this.progress != 0.0f) {
            canvas.drawLine(0.0f, (float) getDimensionInPixel(10), (((float) width) * this.progress) / 100.0f, (float) getDimensionInPixel(10), this.paint);
        }
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), this.strokeWidth);
    }

    private int getDimensionInPixel(int dp) {
        return (int) TypedValue.applyDimension(1, (float) dp, getResources().getDisplayMetrics());
    }
}
