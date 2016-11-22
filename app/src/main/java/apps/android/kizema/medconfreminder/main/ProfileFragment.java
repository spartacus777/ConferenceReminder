package apps.android.kizema.medconfreminder.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import apps.android.kizema.medconfreminder.App;
import apps.android.kizema.medconfreminder.R;
import apps.android.kizema.medconfreminder.auth.ProfilePhotoChooserActivity;
import apps.android.kizema.medconfreminder.auth.SplashActivty;
import apps.android.kizema.medconfreminder.auth.helpers.ImageConstatnts;
import apps.android.kizema.medconfreminder.auth.helpers.PictureGetterDialog;
import apps.android.kizema.medconfreminder.auth.helpers.Session;
import apps.android.kizema.medconfreminder.base.BaseActivity;
import apps.android.kizema.medconfreminder.model.AccountUser;
import apps.android.kizema.medconfreminder.model.AccountUserDao;
import apps.android.kizema.medconfreminder.model.User;
import apps.android.kizema.medconfreminder.model.UserDao;
import apps.android.kizema.medconfreminder.util.ImageLoaderHelper;
import apps.android.kizema.medconfreminder.util.UserHelper;
import apps.android.kizema.medconfreminder.util.validator.NameValidator;
import apps.android.kizema.medconfreminder.util.validator.PasswordValidator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static apps.android.kizema.medconfreminder.auth.helpers.ImageConstatnts.REQUEST_CODE_MAKE_PHOTO;

public class ProfileFragment extends BaseFragment implements PictureGetterDialog.PictureGetterDialogListener {

    @BindView(R.id.etName)
    EditText etName;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.tvSave)
    TextView tvSave;

    @BindView(R.id.tvLogOut)
    TextView tvLogOut;

    @BindView(R.id.ivPerson)
    ImageView ivPerson;

    @BindView(R.id.tvProfile)
    TextView tvProfile;

    private String photoPath = null;
    private PictureGetterDialog pictureGetterDialog;
    private BaseActivity baseActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        init();

        return view;
    }

    private void init(){
        User user = UserHelper.getMyUser();
        etName.setText(Session.getInstance().getNameToken());
        etPassword.setText(Session.getInstance().getPassToken());

        photoPath = user.getPhoto();
        ImageLoader.getInstance().displayImage(ImageLoaderHelper.FILE_SYSTEM_PREF + photoPath, ivPerson);

        if (user.getIsAdmin()){
            tvProfile.setText("Admin's profile");
        } else {
            tvProfile.setText("Doctor's profile");
        }

        pictureGetterDialog = new PictureGetterDialog(getActivity());
        pictureGetterDialog.setPictureGetterDialogListener(this);
    }

    @OnClick(R.id.tvSave)
    public void onSaveClicked() {

        if (!NameValidator.getInstance().validate(etName.getText().toString())){
            Snackbar.make(etName, "Name is invalid", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (!PasswordValidator.getInstance().validate(etPassword.getText().toString())){
            Snackbar.make(etPassword, "Password should be more than 6 symbols and contain at least one digit", Snackbar.LENGTH_SHORT).show();
            return;
        }

        String name = etName.getText().toString();
        String pass = etPassword.getText().toString();

        User user = UserHelper.getMyUser();
        user.setName(name);
        user.setPhoto(photoPath);

        UserDao userDao = App.getDaoSession().getUserDao();
        userDao.update(user);

        AccountUser accountUser = UserHelper.getMyAccUser();
        accountUser.setName(name);
        accountUser.setPassword(pass);

        AccountUserDao accountUserDao = App.getDaoSession().getAccountUserDao();
        accountUserDao.update(accountUser);

        Session.getInstance().saveToken(name, pass);
    }

    @OnClick(R.id.tvLogOut)
    public void onLogOutClicked() {
        Session.getInstance().clearAuthToken();

        Intent intent = new Intent(getActivity(), SplashActivty.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.ivPerson)
    public void onPhotoClick() {
        pictureGetterDialog.show();
    }

    @Override
    public void onCapturePhotoPressed() {
        photoPath = baseActivity.openCameraAcivity();
    }

    @Override
    public void onSelectPhotoFromGalleryPressed() {
        baseActivity.openGalleryAcivity();
    }

    @Override
    public void onDeletePressed() {
        ivPerson.setImageResource(R.drawable.ic_placeholder);
        pictureGetterDialog.cancel();
    }

    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_MAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (photoPath == null && data != null){
                        photoPath = data.getStringExtra(ImageConstatnts.FILE_NAME);
                    }
                    ((MainActivity) baseActivity).openImageEditorActivity(photoPath);
                }
                return;

            case ImageConstatnts.CHOOSER_IMAGE_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    photoPath = data.getStringExtra(ProfilePhotoChooserActivity.PATH);
                    ImageLoader.getInstance().displayImage(ImageLoaderHelper.FILE_SYSTEM_PREF + photoPath, ivPerson);
                    pictureGetterDialog.photoAdded();
                }
                return;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        baseActivity = null;
    }
}
