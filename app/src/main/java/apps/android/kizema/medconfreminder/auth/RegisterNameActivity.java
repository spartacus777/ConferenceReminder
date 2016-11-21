package apps.android.kizema.medconfreminder.auth;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import apps.android.kizema.medconfreminder.R;
import apps.android.kizema.medconfreminder.auth.helpers.ImageConstatnts;
import apps.android.kizema.medconfreminder.auth.helpers.PictureGetterDialog;
import apps.android.kizema.medconfreminder.util.ImageLoaderHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterNameActivity extends AppCompatActivity implements PictureGetterDialog.PictureGetterDialogListener {


    private static final int REQUEST_CODE_GALLERY = 32;
    private static final int REQUEST_CODE_MAKE_PHOTO = 82;
    private static final String IS_ADMIN = "isAdmin";
    private static final String PASS = "pass";
    private static final String EMAIL = "email";

    private String name, email, pass;
    private boolean isAdmin;

    @BindView(R.id.ivPerson)
    ImageView ivPerson;

    @BindView(R.id.etName)
    EditText etName;

    @BindView(R.id.tvNext)
    TextView tvNext;

    private String photoPath;
    private PictureGetterDialog pictureGetterDialog;

    public static Intent getIntent(Activity activity, boolean isAdmin, String pass, String email){
        Intent intent = new Intent(activity, RegisterNameActivity.class);
        intent.putExtra(IS_ADMIN, isAdmin);
        intent.putExtra(PASS, pass);
        intent.putExtra(EMAIL, email);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_name);
        ButterKnife.bind(this);

        isAdmin = getIntent().getBooleanExtra(IS_ADMIN, false);
        pass = getIntent().getStringExtra(PASS);
        email = getIntent().getStringExtra(EMAIL);

        pictureGetterDialog = new PictureGetterDialog(this);
        pictureGetterDialog.setPictureGetterDialogListener(this);
    }

    @OnClick(R.id.ivPerson)
    public void onPhotoClick() {
        pictureGetterDialog.show();
    }

    @Override
    public void onCapturePhotoPressed() {
        photoPath = openCameraAcivity();
    }

    @Override
    public void onSelectPhotoFromGalleryPressed() {
        openGalleryAcivity();
    }

    @Override
    public void onDeletePressed() {
        ivPerson.setImageResource(R.drawable.ic_placeholder);
        pictureGetterDialog.cancel();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_MAKE_PHOTO:

                if (resultCode == RESULT_OK) {
                    if (photoPath == null && data != null){
                        photoPath = data.getStringExtra(ImageConstatnts.FILE_NAME);
                    }

                    openImageEditorActivity();
                }
                return;
            case ImageConstatnts.CHOOSER_IMAGE_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    photoPath = data.getStringExtra(ProfilePhotoChooserActivity.PATH);
                    ImageLoader.getInstance().displayImage(ImageLoaderHelper.FILE_SYSTEM_PREF + photoPath, ivPerson);
                    pictureGetterDialog.photoAdded();
                }
                return;


            case REQUEST_CODE_GALLERY:

                if (resultCode == RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    photoPath = getPath(selectedImageUri);
                    openImageEditorActivity();
                }
                return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void openImageEditorActivity() {
        if (photoPath == null || photoPath.length() == 0){
            Snackbar snackbar = Snackbar
                    .make(ivPerson, "Error while downloading", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return;
        }

        Intent intent = new Intent(this, ProfilePhotoChooserActivity.class);

        if (photoPath.substring(0, 4).equalsIgnoreCase("http")) {
            intent.putExtra(ProfilePhotoChooserActivity.URL, photoPath);
        } else {
            intent.putExtra(ProfilePhotoChooserActivity.PATH, photoPath);
        }
        startActivityForResult(intent, ImageConstatnts.CHOOSER_IMAGE_ACTIVITY);
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
                startActivityForResult(takePictureIntent, REQUEST_CODE_MAKE_PHOTO);
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
                    "Select from gallery"), REQUEST_CODE_GALLERY);
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
