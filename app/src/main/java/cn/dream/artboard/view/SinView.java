package cn.dream.artboard.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SinView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private final String TAG = "SinView";

    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private boolean mIsDrawing;

    private Path mPath;
    private Paint mPaint;

    private int mLineX;
    private int mLineY;

    public SinView(Context context) {
        super(context);
        init();
    }

    public SinView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLUE);
        mPaint.setDither(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mLineX = 0;
        mLineY = 0;
        mPath.moveTo(mLineX, mLineY);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;
        new Thread(this).start();
        Log.d(TAG, "surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
        Log.d(TAG, "surfaceDestroyed");
    }

    @Override
    public void run() {
        while (mIsDrawing) {
            long start = System.currentTimeMillis();
            drawSin();
            long end = System.currentTimeMillis();
            if (end - start < 20) {
                try {
                    Thread.sleep(20 - (end - start));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void drawSin() {
        try {
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null) {
                mCanvas.drawColor(Color.WHITE);
                mCanvas.drawPath(mPath, mPaint);
            }
            mLineX += 1;
            mLineY = (int) (100 * Math.sin(mLineX * 2 * Math.PI / 180) + 400);
            mPath.lineTo(mLineX, mLineY);
        } catch (Exception e) {
            Log.d(TAG, "exception:" + e);
        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }
}

