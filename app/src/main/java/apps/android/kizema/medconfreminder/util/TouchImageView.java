package apps.android.kizema.medconfreminder.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.List;

import apps.android.kizema.medconfreminder.R;


public class TouchImageView extends ImageView implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private static final String TAG = "TouchImageView";

    private static final int ERROR_IMAGE_W = UIHelper.getPixel(600);
    private static final int ERROR_IMAGE_H = UIHelper.getPixel(400);
    private static final int ERROR_IMAGE = R.drawable.ic_placeholder;

    private static final int CLICK = 3;

    // Remember some things for zooming
    private PointF last = new PointF();
    private PointF start = new PointF();
    private float minScale = 1f;
    private float maxScale = 3f;
    private float[] m;
    private int viewWidth, viewHeight;
    private float saveScale = 1f;

    private MODE mode = MODE.NONE;
    private Matrix matrix;
    private int drawableRotation = 0;

    private float origWidth, origHeight;
    private int oldMeasuredWidth, oldMeasuredHeight;
    private ScaleGestureDetector mScaleDetector;

    private String fileName;

    private boolean centerCrop = false;

    private Bitmap readyBitmap;

    private List<TouchImageViewCallback> touchImageViewCallback;
    private OnNoScaleTouchListener onNoScaleTouchListener;
    private OnReadyListener onReadyListener;

    private GestureDetectorCompat mDetector;

    private ImageTouchListener imageTouchListener;

    public enum MODE {
        NONE, DRAG, ZOOM
    }

    public interface TouchImageViewCallback {
        void applyMatrix(Matrix matrix);

        void onBitmapSizesCounted(Matrix m, int w, int h);
    }

    public interface OnReadyListener{
        void onReady();
    }

    public interface OnNoScaleTouchListener {
        void onTouch(MotionEvent event);
    }

    public void setOnNoScaleTouchListener(OnNoScaleTouchListener onNoScaleTouchListener){
        this.onNoScaleTouchListener = onNoScaleTouchListener;
    }

    public TouchImageView(Context context, Bitmap btm) {
        super(context);

        readyBitmap = btm;
        setImageBitmap(readyBitmap);

        touchImageViewCallback = new LinkedList<>();
        sharedConstructing(context);
    }

    public TouchImageView(Context context, Bitmap btm, boolean centerCrop) {
        this(context, btm);
        this.centerCrop = centerCrop;
    }

    public TouchImageView(Context context, int rot, String fileName) {
        super(context);
        this.fileName = fileName;
        this.drawableRotation = rot;

        touchImageViewCallback = new LinkedList<>();
        sharedConstructing(context);
    }

    public TouchImageView(Context context, int rot, String fileName, boolean centerCrop) {
        this(context, rot, fileName);
        this.centerCrop = centerCrop;
    }

    public void addTouchImageViewCallback(TouchImageViewCallback touchImageViewCallback) {
        this.touchImageViewCallback.add(touchImageViewCallback);
    }

    private void sharedConstructing(Context context) {
        super.setClickable(true);

        mDetector = new GestureDetectorCompat(getContext(), this);
        mDetector.setOnDoubleTapListener(this);

        scaleListener = new ScaleListener();
        mScaleDetector = new ScaleGestureDetector(context, scaleListener);
        matrix = new Matrix();
        m = new float[9];
        setImageMatrix(matrix);
        setScaleType(ScaleType.MATRIX);

        imageTouchListener = new ImageTouchListener();
        setOnTouchListener(imageTouchListener);
    }
    ScaleListener scaleListener;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (TouchImageViewCallback callback : touchImageViewCallback) {
            callback.applyMatrix(getImageMatrix());
        }
    }

    public void setMaxZoom(float x) {
        maxScale = x;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mode = MODE.ZOOM;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float mScaleFactor = detector.getScaleFactor();

            float origScale = saveScale;
            saveScale *= mScaleFactor;
            if (saveScale > maxScale) {
                saveScale = maxScale;
                mScaleFactor = maxScale / origScale;
            } else if (saveScale < minScale) {
                saveScale = minScale;
                mScaleFactor = minScale / origScale;
            }

            if (origWidth * saveScale <= viewWidth || origHeight * saveScale <= viewHeight) {
                matrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2, viewHeight / 2);
            } else {
                matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());
            }
            fixTrans();
            return true;
        }
    }


    void fixTrans() {

        matrix.getValues(m);
        float transX = m[Matrix.MTRANS_X];
        float transY = m[Matrix.MTRANS_Y];

        float fixTransX, fixTransY;

        if (isCenterSquare){
            fixTransX = getFixTransCenterSquareW(transX, viewWidth, viewHeight, origWidth * saveScale);
            fixTransY = getFixTransCenterSquareH(transY, viewWidth, viewHeight, origHeight * saveScale);
        } else {
            fixTransX = getFixTrans(transX, viewWidth, origWidth * saveScale);
            fixTransY = getFixTrans(transY, viewHeight, origHeight * saveScale);
        }

        if (fixTransX != 0 || fixTransY != 0) {
            matrix.postTranslate(fixTransX, fixTransY);
        }
    }


    float getFixTrans(float trans, float viewSize, float contentSize) {
        float minTrans, maxTrans;
        if (contentSize <= viewSize) {
            minTrans = 0;
            maxTrans = viewSize - contentSize;
        } else {
            minTrans = viewSize - contentSize;
            maxTrans = 0;
        }

        if (trans < minTrans)
            return -trans + minTrans;
        if (trans > maxTrans)
            return -trans + maxTrans;
        return 0;
    }

    float getFixTransCenterSquareH(float trans, float viewW, float viewH, float contentSize) {
        float minTrans, maxTrans;
        float min = Math.min(viewW, viewH);

        float delta = (viewH - viewW) / 2;

        if (contentSize <= min) {
            minTrans = 0 + delta;
            maxTrans = min - contentSize + delta;
        } else {
            minTrans = min - contentSize + delta;
            maxTrans = 0 + delta;
        }

        if (trans < minTrans)
            return -trans + minTrans;
        if (trans > maxTrans)
            return -trans + maxTrans;
        return 0;
    }


    float getFixTransCenterSquareW(float trans, float viewW, float viewH, float contentSize) {
        float minTrans, maxTrans;
        float min = Math.min(viewW, viewH);

        if (contentSize <= min) {
            minTrans = 0;
            maxTrans = min - contentSize;
        } else {
            minTrans = min - contentSize;
            maxTrans = 0;
        }

        if (trans < minTrans)
            return -trans + minTrans;
        if (trans > maxTrans)
            return -trans + maxTrans;
        return 0;
    }

    float getFixDragTrans(float delta, float viewSize, float contentSize) {
        if (contentSize <= viewSize) {
            return 0;
        }
        return delta;
    }

    private class BitmapWorkerTask extends AsyncTask<Void, Void, Drawable> {
        BitmapFactory.Options options;

        private volatile Context context;
        private volatile Resources res;

        public BitmapWorkerTask(BitmapFactory.Options options) {
            this.options = options;

            context = getContext();
            res = getResources();
        }

        @Override
        protected Drawable doInBackground(Void ... p) {
            options.inJustDecodeBounds = false;
            Bitmap myBitmap = BitmapFactory.decodeFile(fileName, options);

            if (myBitmap == null) {
                return context.getResources().getDrawable(ERROR_IMAGE);
            }

            Drawable dr = new BitmapDrawable(context.getResources(), myBitmap);
            dr = BitmapHelper.resizeRotate(res, dr, myBitmap.getWidth(), myBitmap.getHeight(), drawableRotation);
            return dr;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            Log.d(TAG, " = FINISHED = ");
            setImageDrawable(drawable);
            forceLayout();
        }
    }

    private Point savedDrawableSize = new Point();
    private BitmapWorkerTask task;

    private int c = 0;

    public Point adjustDrawable(){
        ++c;

        if (c > 1){
            return savedDrawableSize;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);

        options.inSampleSize = BitmapHelper.calculateInSampleSize(options, viewWidth/2, (viewHeight  ) / 2);
//        options.inSampleSize = BitmapHelper.calculateInSampleSize(options, viewWidth, viewHeight);
        Log.d(TAG, "viewWidth " + viewWidth + "    viewHeight " + viewHeight + "   options.inSampleSize :"+options.inSampleSize );

        Point ret = new Point();
        ret.x = options.outWidth/options.inSampleSize;
        ret.y = options.outHeight/options.inSampleSize;

        //if we have an error drawable, thene init with default value
        if (ret.x == 0 || ret.y == 0){
            Log.d(TAG, " ret.x == 0 && ret.y == 0 ");
            ret.x = ERROR_IMAGE_W;
            ret.y = ERROR_IMAGE_H;
        }

        savedDrawableSize.x = ret.x;
        savedDrawableSize.y = ret.y;

        if (drawableRotation == 90 || drawableRotation == 270){
            savedDrawableSize.x = ret.y;
            savedDrawableSize.y = ret.x;
        }

        Log.d(TAG, "outWidth : "+options.outWidth + "    outHeight: " +options.outHeight + "   inSampleSize:"+options.inSampleSize);

        if (task != null){
            if (!task.isCancelled()){
                task.cancel(true);
            }
            task = new BitmapWorkerTask(options);
            task.execute();
            return ret;
        }

        task = new BitmapWorkerTask(options);
        task.execute();

        return ret;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        if (oldMeasuredWidth == viewWidth && oldMeasuredHeight == viewHeight
                || viewWidth == 0 || viewHeight == 0)
            return;

        oldMeasuredHeight = viewHeight;
        oldMeasuredWidth = viewWidth;

        if (saveScale == 1) {
            //Fit to screen.
            float scale;
            Point p;

            if (readyBitmap != null && !readyBitmap.isRecycled()){
                p = new Point();
                p.x = readyBitmap.getWidth();
                p.y = readyBitmap.getHeight();
            } else {
                p = adjustDrawable();
            }

            if (getDrawable() == null || getDrawable().getIntrinsicWidth() == 0 || getDrawable().getIntrinsicHeight() == 0) {
                return;
            }

            //TODO drawable is set - notify client

            int bmWidth = p.x;
            int bmHeight = p.y;

            float scaleX = (float) viewWidth / (float) bmWidth;
            float scaleY = (float) viewHeight / (float) bmHeight;

            scale = Math.min(scaleX, scaleY);
            if (centerCrop){
                scale = Math.max(scaleX, scaleY);
            }

            matrix.setScale(scale, scale);
            // Center the image
            float redundantYSpace = (float) viewHeight - (scale * (float) bmHeight);
            float redundantXSpace = (float) viewWidth - (scale * (float) bmWidth);
            redundantYSpace /= (float) 2;
            redundantXSpace /= (float) 2;
            matrix.postTranslate(redundantXSpace, redundantYSpace);
            origWidth = viewWidth - 2 * redundantXSpace;
            origHeight = viewHeight - 2 * redundantYSpace;

            setImageMatrix(matrix);

            for (TouchImageViewCallback callback : touchImageViewCallback) {
                callback.onBitmapSizesCounted(matrix, bmWidth, bmHeight);
            }

            if (onReadyListener != null){
                onReadyListener.onReady();
            }
        }
        fixTrans();
    }

    public void setOnReadyListener(OnReadyListener onReadyListener){
        this.onReadyListener = onReadyListener;
    }

    public void sendOnTouch(MotionEvent ev){
        imageTouchListener.onTouch(this, ev);
    }

    private class ImageTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mScaleDetector.onTouchEvent(event);
            mDetector.onTouchEvent(event);
            PointF curr = new PointF(event.getX(), event.getY());

            if (saveScale == 1 && onNoScaleTouchListener != null){
                onNoScaleTouchListener.onTouch(event);
                return true;
            }

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    Log.v("TOUCH", "ACTION_DOWN");
                    last.set(curr);
                    start.set(last);
                    mode = MODE.DRAG;
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.v("TOUCH", "ACTION_MOVE");

                    if (event.getPointerCount() >= 2 || mode == MODE.DRAG) {
                        float deltaX = curr.x - last.x;
                        float deltaY = curr.y - last.y;

                        float fixTransX, fixTransY;

                        if (isCenterSquare) {
                            if (viewHeight > viewWidth) {
                                fixTransX = getFixDragTrans(deltaX, viewWidth, origWidth * saveScale);
                                fixTransY = deltaY;
                            } else {
                                fixTransX = deltaX;
                                fixTransY = getFixDragTrans(deltaY, viewHeight, origHeight * saveScale);
                            }
                        } else {
                            fixTransX = getFixDragTrans(deltaX, viewWidth, origWidth * saveScale);
                            fixTransY = getFixDragTrans(deltaY, viewHeight, origHeight * saveScale);
                        }

                        matrix.postTranslate(fixTransX, fixTransY);

                        fixTrans();
                        last.set(curr.x, curr.y);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.v("TOUCH", "ACTION_UP");
                    mode = MODE.NONE;
                    int xDiff = (int) Math.abs(curr.x - start.x);
                    int yDiff = (int) Math.abs(curr.y - start.y);
                    if (xDiff < CLICK && yDiff < CLICK)
                        performClick();
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    Log.v("TOUCH", "ACTION_POINTER_UP");
                    mode = MODE.NONE;
                    break;
            }

            setImageMatrix(matrix);
            return true; // indicate event was handled
        }
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    /**
     * This method is responsible for TouchImageView scale up and down on double tap gesture
     * @param e
     * @return
     */
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (saveScale > 1 ){
            //scale down;
            if (getDrawable() == null || getDrawable().getIntrinsicWidth() == 0 ||
                    getDrawable().getIntrinsicHeight() == 0){
                return true;
            }

            scaleViewDown();

        } else {
            //scale up
            scaleViewUp(e);
        }

        return true;
    }

    public void scaleViewDown(){
        Log.d(TAG, "scale DOWN");

        saveScale = 1;
        float scaleX = (float) viewWidth / (float) getDrawable().getIntrinsicWidth();
        float scaleY = (float) viewHeight / (float) getDrawable().getIntrinsicHeight();
        float scale = Math.min(scaleX, scaleY);
        matrix.setScale(scale, scale);

        // Center the image
        float redundantYSpace = (float) viewHeight - (scale * (float)getDrawable().getIntrinsicHeight());
        float redundantXSpace = (float) viewWidth - (scale * (float) getDrawable().getIntrinsicWidth());
        redundantYSpace /= (float) 2;
        redundantXSpace /= (float) 2;
        matrix.postTranslate(redundantXSpace, redundantYSpace);
        origWidth = viewWidth - 2 * redundantXSpace;
        origHeight = viewHeight - 2 * redundantYSpace;

        setImageMatrix(matrix);
        fixTrans();
    }

    public void scaleViewUp(MotionEvent e){
        Log.d(TAG, "scale UP");
        float curScale = maxScale/2;

        float mScaleFactor = curScale/saveScale;
        float origScale = saveScale;
        saveScale *= mScaleFactor;
        if (saveScale > maxScale) {
            saveScale = maxScale;
            mScaleFactor = maxScale / origScale;
        } else if (saveScale < minScale) {
            saveScale = minScale;
            mScaleFactor = minScale / origScale;
        }

        if (origWidth * saveScale <= viewWidth || origHeight * saveScale <= viewHeight) {
            matrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2, viewHeight / 2);
        } else {
            matrix.postScale(mScaleFactor, mScaleFactor, e.getRawX(), e.getRawY());
        }

        fixTrans();
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    private boolean isCenterSquare = false;

    /**
     * Used in MiniatureAvatarChooser
     */
    public void setCenterSquare(boolean isCenterSquare){
        this.isCenterSquare = isCenterSquare;
    }

}
