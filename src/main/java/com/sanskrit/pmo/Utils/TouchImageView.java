package com.sanskrit.pmo.Utils;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.widget.ImageView;

public class TouchImageView extends ImageView {
    static final int CLICK = 3;
    static final int DRAG = 1;
    static final int NONE = 0;
    static final int ZOOM = 2;
    Context context;
    PointF last = new PointF();
    float[] f156m;
    ScaleGestureDetector mScaleDetector;
    Matrix matrix;
    float maxScale = 3.0f;
    float minScale = 1.0f;
    int mode = 0;
    int oldMeasuredHeight;
    int oldMeasuredWidth;
    protected float origHeight;
    protected float origWidth;
    float saveScale = 1.0f;
    PointF start = new PointF();
    int viewHeight;
    int viewWidth;

    class C13221 implements OnTouchListener {
        C13221() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            TouchImageView.this.mScaleDetector.onTouchEvent(event);
            PointF curr = new PointF(event.getX(), event.getY());
            switch (event.getAction()) {
                case 0:
                    TouchImageView.this.last.set(curr);
                    TouchImageView.this.start.set(TouchImageView.this.last);
                    TouchImageView.this.mode = 1;
                    break;
                case 1:
                    TouchImageView.this.mode = 0;
                    int yDiff = (int) Math.abs(curr.y - TouchImageView.this.start.y);
                    if (((int) Math.abs(curr.x - TouchImageView.this.start.x)) < 3 && yDiff < 3) {
                        TouchImageView.this.performClick();
                        break;
                    }
                case 2:
                    if (TouchImageView.this.mode == 1) {
                        float deltaY = curr.y - TouchImageView.this.last.y;
                        TouchImageView.this.matrix.postTranslate(TouchImageView.this.getFixDragTrans(curr.x - TouchImageView.this.last.x, (float) TouchImageView.this.viewWidth, TouchImageView.this.origWidth * TouchImageView.this.saveScale), TouchImageView.this.getFixDragTrans(deltaY, (float) TouchImageView.this.viewHeight, TouchImageView.this.origHeight * TouchImageView.this.saveScale));
                        TouchImageView.this.fixTrans();
                        TouchImageView.this.last.set(curr.x, curr.y);
                        break;
                    }
                    break;
                case 6:
                    TouchImageView.this.mode = 0;
                    break;
            }
            TouchImageView.this.setImageMatrix(TouchImageView.this.matrix);
            TouchImageView.this.invalidate();
            return true;
        }
    }

    private class ScaleListener extends SimpleOnScaleGestureListener {
        private ScaleListener() {
        }

        public boolean onScaleBegin(ScaleGestureDetector detector) {
            TouchImageView.this.mode = 2;
            return true;
        }

        public boolean onScale(ScaleGestureDetector detector) {
            float mScaleFactor = detector.getScaleFactor();
            float origScale = TouchImageView.this.saveScale;
            TouchImageView touchImageView = TouchImageView.this;
            touchImageView.saveScale *= mScaleFactor;
            if (TouchImageView.this.saveScale > TouchImageView.this.maxScale) {
                TouchImageView.this.saveScale = TouchImageView.this.maxScale;
                mScaleFactor = TouchImageView.this.maxScale / origScale;
            } else if (TouchImageView.this.saveScale < TouchImageView.this.minScale) {
                TouchImageView.this.saveScale = TouchImageView.this.minScale;
                mScaleFactor = TouchImageView.this.minScale / origScale;
            }
            if (TouchImageView.this.origWidth * TouchImageView.this.saveScale <= ((float) TouchImageView.this.viewWidth) || TouchImageView.this.origHeight * TouchImageView.this.saveScale <= ((float) TouchImageView.this.viewHeight)) {
                TouchImageView.this.matrix.postScale(mScaleFactor, mScaleFactor, (float) (TouchImageView.this.viewWidth / 2), (float) (TouchImageView.this.viewHeight / 2));
            } else {
                TouchImageView.this.matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());
            }
            TouchImageView.this.fixTrans();
            return true;
        }
    }

    public TouchImageView(Context context) {
        super(context);
        sharedConstructing(context);
    }

    public TouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        sharedConstructing(context);
    }

    private void sharedConstructing(Context context) {
        super.setClickable(true);
        this.context = context;
        this.mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        this.matrix = new Matrix();
        this.f156m = new float[9];
        setImageMatrix(this.matrix);
        setScaleType(ScaleType.MATRIX);
        setOnTouchListener(new C13221());
    }

    public void setMaxZoom(float x) {
        this.maxScale = x;
    }

    void fixTrans() {
        this.matrix.getValues(this.f156m);
        float transX = this.f156m[2];
        float transY = this.f156m[5];
        float fixTransX = getFixTrans(transX, (float) this.viewWidth, this.origWidth * this.saveScale);
        float fixTransY = getFixTrans(transY, (float) this.viewHeight, this.origHeight * this.saveScale);
        if (fixTransX != 0.0f || fixTransY != 0.0f) {
            this.matrix.postTranslate(fixTransX, fixTransY);
        }
    }

    float getFixTrans(float trans, float viewSize, float contentSize) {
        float minTrans;
        float maxTrans;
        if (contentSize <= viewSize) {
            minTrans = 0.0f;
            maxTrans = viewSize - contentSize;
        } else {
            minTrans = viewSize - contentSize;
            maxTrans = 0.0f;
        }
        if (trans < minTrans) {
            return (-trans) + minTrans;
        }
        if (trans > maxTrans) {
            return (-trans) + maxTrans;
        }
        return 0.0f;
    }

    float getFixDragTrans(float delta, float viewSize, float contentSize) {
        if (contentSize <= viewSize) {
            return 0.0f;
        }
        return delta;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        this.viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        if ((this.oldMeasuredHeight != this.viewWidth || this.oldMeasuredHeight != this.viewHeight) && this.viewWidth != 0 && this.viewHeight != 0) {
            this.oldMeasuredHeight = this.viewHeight;
            this.oldMeasuredWidth = this.viewWidth;
            if (this.saveScale == 1.0f) {
                Drawable drawable = getDrawable();
                if (drawable != null && drawable.getIntrinsicWidth() != 0 && drawable.getIntrinsicHeight() != 0) {
                    int bmWidth = drawable.getIntrinsicWidth();
                    int bmHeight = drawable.getIntrinsicHeight();
                    Log.d("bmSize", "bmWidth: " + bmWidth + " bmHeight : " + bmHeight);
                    float scale = Math.min(((float) this.viewWidth) / ((float) bmWidth), ((float) this.viewHeight) / ((float) bmHeight));
                    this.matrix.setScale(scale, scale);
                    float redundantYSpace = (((float) this.viewHeight) - (((float) bmHeight) * scale)) / 2.0f;
                    float redundantXSpace = (((float) this.viewWidth) - (((float) bmWidth) * scale)) / 2.0f;
                    this.matrix.postTranslate(redundantXSpace, redundantYSpace);
                    this.origWidth = ((float) this.viewWidth) - (2.0f * redundantXSpace);
                    this.origHeight = ((float) this.viewHeight) - (2.0f * redundantYSpace);
                    setImageMatrix(this.matrix);
                } else {
                    return;
                }
            }
            fixTrans();
        }
    }
}
