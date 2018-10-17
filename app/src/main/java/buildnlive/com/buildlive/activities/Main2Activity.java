package buildnlive.com.buildlive.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import buildnlive.com.buildlive.R;

public class Main2Activity extends AppCompatActivity {

    Context mContext;
    private Paint mPaint;
    MaskFilter mEmboss;
    MaskFilter mBlur;
    private RelativeLayout mPaintBaseLayout;
    private LinearLayout mPaintBaseLayout2;
    private PaintView mPaintView;

    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix matrix1 = new Matrix();
    Matrix savedMatrix = new Matrix();
    Matrix savedMatrix2 = new Matrix();
    Matrix dmMtrx = new Matrix();

    private int WIDTH = 0;
    private int HEIGHT = 1;

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int POINT2 = 2;
    static final int ZOOM = 3;
    int mode = NONE;
    private static final float MIN_ZOOM = 0.0f;
    private static final float MAX_ZOOM = 1.0f;


    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    float newDist;
    float distanceOffset = 50f;
    float minOffset = 50f;
    float maxOffset = 10000f;
    private boolean falg = true;
    private int startval = 0;
    Bitmap newbm;
    private float finalscale;
    Bitmap bm;
    private float scaledImageOffsetX;
    private float scaledImageOffsetY;
    //ImageView imageView;
    ProgressDialog dialog;
    private float[] matrixValues;
    Bitmap temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        this.initialize();

        this.PaintSet();
        //imageView = (ImageView) findViewById(R.id.imageview1);
        Button button = (Button) findViewById(R.id.btnzoom);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (falg) {
                    getFlag(false);
                } else {
                    getFlag(true);
                }
            }
        });

        Button btnset = (Button) findViewById(R.id.btnset);
        btnset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startval = 1;
                mPaintBaseLayout.setDrawingCacheEnabled(true);

                //imageView.setVisibility(View.VISIBLE);

                mPaintBaseLayout.setVisibility(View.GONE);
                new SaveImageAsynk().execute();
            }
        });

        /*imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView view = (ImageView) v;

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        savedMatrix.set(matrix1);
                        start.set(event.getX(), event.getY());
                        mode = DRAG;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        oldDist = spacing(event);
                        if (oldDist > 10f) {
                            start.set(event.getX(), event.getY());
                            savedMatrix.set(matrix1);
                            midPoint(mid, event);
                            // mode = POINT2;
                            mode = ZOOM;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        mode = NONE;
                        distanceOffset = minOffset;
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        distanceOffset = minOffset;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == POINT2) {
                            newDist = spacing(event);
                            if (newDist - oldDist > 5f
                                    || newDist - oldDist < -5f) {
                                mode = ZOOM;
                            } else {
                                start.set(event.getX(), event.getY());
                                mode = DRAG;
                            }
                        } else if (mode == DRAG) {
                            matrix1.set(savedMatrix);
                            matrix1.postTranslate(event.getX() - start.x,
                                    event.getY() - start.y);
                        } else if (mode == ZOOM) {
                            newDist = spacing(event);
                            if (newDist > 10f) {
                                matrix1.set(savedMatrix);
                                float scale = newDist / oldDist;
                                matrix1.postScale(scale, scale, mid.x,
                                        mid.y);
                                finalscale = scale;
                            }
                        }
                        break;
                }

                view.setImageMatrix(matrix1);
//    matrixTurning(matrix1, view);
                return true; // indicate event was handled
            }
        });*/
    }

    class SaveImageAsynk extends AsyncTask<String, String, String> {
        Bitmap tempBm;

        @Override
        protected String doInBackground(String... params) {
            temp = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, false);
            newbm = Bitmap.createBitmap(mPaintBaseLayout.getWidth(),
                    mPaintBaseLayout.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newbm);
            mPaintBaseLayout.draw(canvas);
            tempBm = combineImages(newbm, temp);
            if (newbm != null) {
                newbm = null;
            }
            if (temp != null) {
                temp = null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //imageView.setImageBitmap(tempBm);
            try {
                dialog.dismiss();
                if (dialog != null) {
                    dialog = null;
                }
            } catch (Exception e) {
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Main2Activity.this);
            dialog.setMessage("Loading...");
            dialog.show();
        }

    }

    public Bitmap combineImages(Bitmap c, Bitmap s) {
        Bitmap cs = null;

        int width, height = 0;

        if (c.getWidth() > s.getWidth()) {
            width = c.getWidth();
            height = c.getHeight() + s.getHeight();
        } else {
            width = s.getWidth();
            height = c.getHeight() + s.getHeight();
        }
        Log.e("hw :", "X = " + width + " Y = " + height);
        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);
        comboImage.drawBitmap(s, new Matrix(), null);
        comboImage.drawBitmap(c, Math.abs(scaledImageOffsetX), Math.abs(scaledImageOffsetY), null);

        return cs;
    }

    private void initialize() {
        mPaintBaseLayout = (RelativeLayout) findViewById(R.id.paint_paint_base_layout);
        mPaintBaseLayout2 = (LinearLayout) findViewById(R.id.paint_paint_base_layout2);

        mContext = this;
        mPaint = new Paint();
        mPaintView = new PaintView(mContext);
        mPaintView.setBackgroundColor(Color.TRANSPARENT);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        mPaintView.setLayoutParams(params);
        mPaintBaseLayout.addView(mPaintView);
        //mPaintBaseLayout.addView(mPaintView, new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mPaintBaseLayout.setBackgroundColor(Color.TRANSPARENT);

//  mPaintView.setScaleType(ScaleType.FIT_XY);
        mPaintView.setAdjustViewBounds(true);
        mPaintView.setMPaint(mPaint);
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.back);

        mPaintView.setImageBitmap(bm);

        mPaintView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                PaintView view = (PaintView) v;
                view.setScaleType(ImageView.ScaleType.MATRIX);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        if (falg) {
                            savedMatrix.set(matrix);
                            start.set(event.getX(), event.getY());
                            mode = DRAG;
                        } else {
                            view.onTouchEvent(event);
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        if (falg) {
                            oldDist = spacing(event);
                            if (oldDist > 10f) {
                                start.set(event.getX(), event.getY());
                                savedMatrix.set(matrix);
                                midPoint(mid, event);
                                mode = ZOOM;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (falg) {
                            mode = NONE;
                            distanceOffset = minOffset;
                        }
                    case MotionEvent.ACTION_POINTER_UP:
                        if (falg) {
                            mode = NONE;
                            distanceOffset = minOffset;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (falg) {
                            if (mode == POINT2) {
                                newDist = spacing(event);
                                if (newDist - oldDist > 5f
                                        || newDist - oldDist < -5f) {
                                    mode = ZOOM;
                                } else {
                                    start.set(event.getX(), event.getY());
                                    mode = DRAG;


                                }
                            } else if (mode == DRAG) {
                                matrix.set(savedMatrix);
                                matrix.postTranslate(event.getX() - start.x,
                                        event.getY() - start.y);
                            } else if (mode == ZOOM) {
                                newDist = spacing(event);
                                if (newDist > 10f) {
                                    matrix.set(savedMatrix);
                                    float scale = newDist / oldDist;
                                    matrix.postScale(scale, scale, mid.x, mid.y);
                                    finalscale = scale;
                                }
                            }
                        } else {
                            view.onTouchEvent(event);
                        }
                        break;
                }

                limitZoom(matrix);
                view.setImageMatrix(matrix);

                matrixTurning(matrix, view);
                RectF r = new RectF();
                matrix.mapRect(r);
                scaledImageOffsetX = r.left;
                scaledImageOffsetY = r.top;

                return true;
            }
        });
    }


    private void limitZoom(Matrix m) {

        float[] values = new float[9];
        m.getValues(values);
        float scaleX = values[Matrix.MSCALE_X];
        float scaleY = values[Matrix.MSCALE_Y];
        if (scaleX > MAX_ZOOM) {
            scaleX = MAX_ZOOM;
        } else if (scaleX < MIN_ZOOM) {
            scaleX = MIN_ZOOM;
        }

        if (scaleY > MAX_ZOOM) {
            scaleY = MAX_ZOOM;
        } else if (scaleY < MIN_ZOOM) {
            scaleY = MIN_ZOOM;
        }

        values[Matrix.MSCALE_X] = scaleX;
        values[Matrix.MSCALE_Y] = scaleY;
        m.setValues(values);
    }

    public boolean getFlag(boolean b) {
        return falg = b;
    }

    /**
     * Determine the space between the first two fingers
     */
    private static float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private static void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private void matrixTurning(Matrix matrix, ImageView view) {

        float[] value = new float[9];
        matrix.getValues(value);
        float[] savedValue = new float[9];
        savedMatrix2.getValues(savedValue);

        // view size
        int width = view.getWidth();
        int height = view.getHeight();

        // image size
        Drawable d = view.getDrawable();
        if (d == null)
            return;
        int imageWidth = d.getIntrinsicWidth();
        int imageHeight = d.getIntrinsicHeight();
        int scaleWidth = (int) (imageWidth * value[0]);
        int scaleHeight = (int) (imageHeight * value[0]);

        if (value[2] < width - scaleWidth)
            value[2] = width - scaleWidth;
        if (value[5] < height - scaleHeight)
            value[5] = height - scaleHeight;
        if (value[2] > 0)
            value[2] = 0;
        if (value[5] > 0)
            value[5] = 0;

        if (value[0] > 10 || value[4] > 10) {
            value[0] = savedValue[0];
            value[4] = savedValue[4];
            value[2] = savedValue[2];
            value[5] = savedValue[5];
        }

        if (imageWidth > width || imageHeight > height) {

            if (scaleWidth < width && scaleHeight < height) {
                int target = WIDTH;

                if (imageWidth < imageHeight)
                    target = HEIGHT;

                if (target == WIDTH)
                    value[0] = value[4] = (float) width / imageWidth;
                if (target == HEIGHT)
                    value[0] = value[4] = (float) height / imageHeight;

                scaleWidth = (int) (imageWidth * value[0]);
                scaleHeight = (int) (imageHeight * value[4]);

                if (scaleWidth == width)
                    value[0] = value[4] = (float) width / imageWidth;
                if (scaleHeight == height)
                    value[0] = value[4] = (float) height / imageHeight;
            }

        } else {
            if (value[0] < 1)
                value[0] = 1;
            if (value[4] < 1)
                value[4] = 1;
        }

        scaleWidth = (int) (imageWidth * value[0]);
        scaleHeight = (int) (imageHeight * value[4]);

        if (scaleWidth < width) {
            value[2] = (float) width / 2 - (float) scaleWidth / 2;
        }
        if (scaleHeight < height) {
            value[5] = (float) height / 2 - (float) scaleHeight / 2;
        }

        matrix.setValues(value);
        savedMatrix2.set(matrix);

    }

    public void PaintSet() {

        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);

        // getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        mEmboss = new EmbossMaskFilter(new float[]{1, 1, 1}, 0.4f, 6, 3.5f);
        mBlur = new BlurMaskFilter(24, BlurMaskFilter.Blur.NORMAL);
    }

    public void colorChanged(int color) {
        mPaint.setColor(color);
    }

}