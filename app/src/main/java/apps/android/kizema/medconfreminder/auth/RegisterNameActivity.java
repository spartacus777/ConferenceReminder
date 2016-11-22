package apps.android.kizema.medconfreminder.auth;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import apps.android.kizema.medconfreminder.base.BaseActivity;
import apps.android.kizema.medconfreminder.CoreService;
import apps.android.kizema.medconfreminder.main.MainActivity;
import apps.android.kizema.medconfreminder.R;
import apps.android.kizema.medconfreminder.auth.control.AuthServerApi;
import apps.android.kizema.medconfreminder.auth.helpers.ImageConstatnts;
import apps.android.kizema.medconfreminder.auth.helpers.PictureGetterDialog;
import apps.android.kizema.medconfreminder.util.ImageLoaderHelper;
import apps.android.kizema.medconfreminder.util.validator.NameValidator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterNameActivity extends BaseActivity implements PictureGetterDialog.PictureGetterDialogListener {

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

    private String photoPath = null;
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

    @OnClick(R.id.tvNext)
    public void onRegisterClick() {
        if (!NameValidator.getInstance().validate(etName.getText().toString())){
            Snackbar snackbar = Snackbar
                    .make(etName, "Name should be not empty and less then 20 chars", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return;
        }

        showProgress();
        CoreService.getInstance().getAuthServerApi().register(etName.getText().toString(), email, pass, photoPath, isAdmin, new AuthServerApi.OnRegisterListener() {
            @Override
            public void onRegistered() {
                hideProgress();

                Intent intent = MainActivity.getIntent(RegisterNameActivity.this);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onError(Object err) {
                hideProgress();
                Snackbar.make(etName, R.string.no_inet, Snackbar.LENGTH_SHORT).show();
            }
        });
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
        intent.putExtra(ProfilePhotoChooserActivity.PATH, photoPath);

        startActivityForResult(intent, ImageConstatnts.CHOOSER_IMAGE_ACTIVITY);
    }

}
