package apps.android.kizema.medconfreminder.auth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import java.io.File;

import apps.android.kizema.medconfreminder.base.BaseActivity;
import apps.android.kizema.medconfreminder.R;
import apps.android.kizema.medconfreminder.util.BitmapHelper;
import apps.android.kizema.medconfreminder.util.ExifHelper;
import apps.android.kizema.medconfreminder.util.TouchImageView;

public class ProfilePhotoChooserActivity extends BaseActivity {

    private static final String TAG = ProfilePhotoChooserActivity.class.getSimpleName();
    public static final String PATH = "PATH_OF_IMAGE";

    private String path;

    private ViewGroup llImageContainer;
    private ImageView ivCancel, ivOk, progress;
    private View space;

    private TouchImageView touchImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_photo_chooser);

        path = getIntent().getStringExtra(PATH);
        Log.d(TAG, "photoPath : " + path);

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

        showProgress();

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

    private void calculateResult() {
        if (touchImageView == null || touchImageView.getDrawable() == null ||
                touchImageView.getDrawable().getIntrinsicWidth() == 0 ||
                touchImageView.getDrawable().getIntrinsicHeight() == 0) {
            return;
        }

        showProgress();
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

        llImageContainer.removeAllViews();
        llImageContainer.addView(touchImageView);
        llImageContainer.addView(frameDeco);

        ProfilePhotoChooserActivity.this.hideProgress();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

}
