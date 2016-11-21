package apps.android.kizema.medconfreminder.auth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import java.io.File;

import apps.android.kizema.medconfreminder.R;
import apps.android.kizema.medconfreminder.util.BitmapHelper;
import apps.android.kizema.medconfreminder.util.ExifHelper;
import apps.android.kizema.medconfreminder.util.TouchImageView;

public class ProfilePhotoChooserActivity extends AppCompatActivity {

    private static final String TAG = ProfilePhotoChooserActivity.class.getSimpleName();
    public static final String PATH = "PATH_OF_IMAGE";
    public static final String URL = "URL";
    public static final String NO_BACK = "NO_BACK";


    private String path;
    private String url;

    private ViewGroup llImageContainer;
    private ImageView ivCancel, ivOk;
    private View space;

    private TouchImageView touchImageView;

    private ImageView progress;

    private boolean noBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_photo_chooser);

        path = getIntent().getStringExtra(PATH);
        Log.d(TAG, "photoPath : " + path);

        if (path == null) {
            url = getIntent().getStringExtra(URL);
            noBack = getIntent().getBooleanExtra(NO_BACK, false);
        }

        llImageContainer = (ViewGroup) findViewById(R.id.llImageContainer);
        startAnimateWheel();

        ivOk = (ImageView) findViewById(R.id.ivOk);
        ivOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateResult();
            }
        });

        space = findViewById(R.id.space);
        ivCancel = (ImageView) findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        if (noBack) {
            ivCancel.setVisibility(View.GONE);
            space.setVisibility(View.GONE);
        }

//        showProgress(false);

        if (path != null && !BitmapHelper.isJpeg(path)) {
            new BitmapHelper.ToJpegTask(path, new BitmapHelper.OnFileSavedListener() {
                @Override
                public void onComplete(File file) {
                    path = file.getAbsolutePath();
                    reinitTouchImageView();
                }
            }).execute();
        } else {
            reinitTouchImageView();
        }
    }

    private void startAnimateWheel() {
        progress = (ImageView) findViewById(R.id.progress);
        Animation anim = new RotateAnimation(360.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(3000);
        progress.startAnimation(anim);
    }

    private void onRotateBtnClicked() {
        if (touchImageView.getDrawable() == null) {
            return;
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap btm = BitmapHelper.drawableToBitmap(touchImageView.getDrawable());
        Bitmap bitmap = Bitmap.createBitmap(btm, 0, 0, btm.getWidth(), btm.getHeight(), matrix, true);
        touchImageView.setImageBitmap(bitmap);
        touchImageView.scaleViewDown();
        touchImageView.scaleViewUp(MotionEvent.obtain(0, 0, 0, 0, 0, 0));
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode){
//            case ImageConstatnts.REQUEST_CODE_MAKE_PHOTO:
//                if (resultCode == RESULT_OK){
//                    path = data.getStringExtra(ImageConstatnts.FILE_NAME);
//                    reinitTouchImageView();
//                }
//                break;
//            case ImageConstatnts.REQUEST_CODE_GALLERY:
//                if (resultCode == RESULT_OK){
//                    path = data.getStringExtra(ImageConstatnts.FILE_NAME);
//                    reinitTouchImageView();
//                }
//                break;
//        }
//    }

    private void calculateResult() {
        if (touchImageView == null || touchImageView.getDrawable() == null ||
                touchImageView.getDrawable().getIntrinsicWidth() == 0 ||
                touchImageView.getDrawable().getIntrinsicHeight() == 0) {
            return;
        }

//        showProgress(false);
        BitmapHelper.SaveCropImageViewBitmapAsyncTask task = new BitmapHelper.SaveCropImageViewBitmapAsyncTask
                (touchImageView, new BitmapHelper.OnFileSavedListener() {

                    @Override
                    public void onComplete(File file) {
                        Intent intent = new Intent();
                        intent.putExtra(PATH, file.getAbsolutePath());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });

        task.execute();


//        Drawable drawable = touchImageView.getDrawable();
//        Rect imageBounds = drawable.getBounds();
//
//        float w = touchImageView.getWidth();
//        float h = touchImageView.getHeight();
//
//        float bw = touchImageView.getDrawable().getIntrinsicWidth();
//        float bh = touchImageView.getDrawable().getIntrinsicHeight();
//        if (w/h < bw/bh){
//            //bitmap is longer than window
//
//        }
//
//        Rect r = new Rect();
//        touchImageView.getDrawingRect(r);
//        Log.d("Edit", "rect :  l:" + r.left + "   t:" + r.top + "   r:" + r.right + "   b:" + r.bottom);
//        Log.d("Edit", "bw:"+bw+"   bh:"+bh);
//        Log.d("Edit", "w:"+w+"   h:"+h);
//
//        Bitmap original = BitmapHelper.drawableToBitmap(touchImageView.getDrawable());
//        Bitmap result = Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), touchImageView.getImageMatrix(), true);
//        File file = BitmapHelper.bitmapToFile(result);
//
//        Intent intent = new Intent();
//        intent.putExtra(PATH, file.getAbsolutePath());
//        setResult(RESULT_OK, intent);
//        finish();
    }

    private void reinitTouchImageView() {
        touchImageView = new TouchImageView(this, ExifHelper.getPhotoRotation(path), path, false);
        afterInitTouchImageView();
    }

    private void afterInitTouchImageView() {
        touchImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        touchImageView.setMaxZoom(4f);

        ImageView frameDeco = new ImageView(this);
        frameDeco.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        frameDeco.setBackgroundResource(R.drawable.rounded_corners);

        llImageContainer.removeAllViews();
        llImageContainer.addView(touchImageView);
        llImageContainer.addView(frameDeco);

//        ProfilePhotoChooserActivity.this.hideProgress();
    }

    @Override
    public void onBackPressed() {
        if (noBack) {
            return;
        }

        setResult(RESULT_CANCELED);
        finish();
    }
}
