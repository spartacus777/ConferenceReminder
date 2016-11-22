package apps.android.kizema.medconfreminder.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import apps.android.kizema.medconfreminder.R;
import apps.android.kizema.medconfreminder.auth.helpers.ImageConstatnts;

/**
 * Created by A.Kizema on 22.11.2016.
 */

public class BaseActivity extends AppCompatActivity {

    private Dialog progressDialog;
    private boolean isShownDialog = false;
    private View progress;
    private Animation anim;

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.tab_activity_transition_in, R.anim.tab_activity_transition_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.tab_activity_transition_in, R.anim.tab_activity_transition_out);
    }

    private void initAnimation(){
        if (anim == null) {
            anim = new RotateAnimation(360.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setInterpolator(new LinearInterpolator());
            anim.setRepeatCount(Animation.INFINITE);
            anim.setDuration(3000);
        }
    }

    protected int getDialogViewResource(){
        return R.layout.dialog_progress;
    }

    public void showProgress() {
        //hideKeyBoard();

        if (progressDialog  == null){
            initAnimation();

            progressDialog = new Dialog(this);
            progressDialog.setCancelable(false);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setContentView(getDialogViewResource());
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            progress = progressDialog.getWindow().findViewById(R.id.progress);
        }

        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    BaseActivity.this.finish();
                }
                return false;
            }
        });

        if (isShownDialog) {
            return;
        }
        isShownDialog = true;

        if (android.os.Build.VERSION.SDK_INT >= 17) {
            if (!isDestroyed()) {
                progressDialog.show();
                animateView();
            }
        } else if (!isFinishing()) {
            progressDialog.show();
            animateView();
        }
    }

    private void animateView(){
        progress.startAnimation(anim);
    }

    private void stopAnimateView(){
        progress.clearAnimation();
    }

    public void hideProgress() {
        if (!isShownDialog) {
            return;
        }

        stopAnimateView();

        isShownDialog = false;
        //we have to check isDestroyed(), @see http://stackoverflow.com/questions/22924825/view-not-attached-to-window-manager-crash
        if (progressDialog != null && progressDialog.isShowing()) {
            if (android.os.Build.VERSION.SDK_INT >= 17) {
                if (!isDestroyed()) {
                    progressDialog.dismiss();
                }
            } else if (!isFinishing()) {
                progressDialog.dismiss();
            }
        }
    }



    public String openCameraAcivity(){
        String filePath = null;

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String imageFileName = "JPEG" + timeStamp;
                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                photoFile = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );

                // Save a file: path for use with ACTION_VIEW intents
                filePath = photoFile.getAbsolutePath();
            } catch (IOException ex) {
                filePath = null;
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, ImageConstatnts.REQUEST_CODE_MAKE_PHOTO);
            }
        }

        return filePath;
    }

    public void openGalleryAcivity(){
        Intent intent = new Intent(Intent.ACTION_PICK);

        intent.setType("image/*");

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        boolean isIntentSafe = activities.size() > 0;

        if (isIntentSafe){
            startActivityForResult(Intent.createChooser(intent,
                    "Select from gallery"), ImageConstatnts.REQUEST_CODE_GALLERY);
        }
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            Log.d("RTY", "  if( uri == null )");
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }
}
