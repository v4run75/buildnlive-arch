package buildnlive.com.buildlive.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

class PaintView extends AppCompatImageView {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;

    // onDraw
    private Paint mPaint;

    // onTouch
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBitmap = Bitmap.createBitmap(1024, 1024, Bitmap.Config.ARGB_8888);
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // canvas.drawColor(0xFFAAAAAA);
        super.onDraw(canvas);
        mCanvas = canvas;
        // canvas = mCanvas;
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        // canvas.drawBitmap(mBitmap, PaintScreen.matrix, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);

    }

    public void clear() {
        mPaint.reset();
        // invalidate();
    }

    public void setMPaint(Paint paint) {
        mPaint = paint;
    }

    private void touchStart(float x, float y) {
        // mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        // kill this so we don't double draw
        mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        Log.d("PaintView", "ev ->" + event.getAction());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }
        return true;
    }

    public void cMatrix(Matrix matrix) {
        mCanvas.setMatrix(matrix);
    }
}

