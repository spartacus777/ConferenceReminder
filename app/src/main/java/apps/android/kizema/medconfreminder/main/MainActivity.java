package apps.android.kizema.medconfreminder.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import apps.android.kizema.medconfreminder.R;
import apps.android.kizema.medconfreminder.auth.ProfilePhotoChooserActivity;
import apps.android.kizema.medconfreminder.auth.helpers.ImageConstatnts;
import apps.android.kizema.medconfreminder.base.BaseActivity;
import apps.android.kizema.medconfreminder.main.ViewPagerAdapter.DataHolder;
import apps.android.kizema.medconfreminder.util.DBSetter;
import butterknife.BindView;
import butterknife.ButterKnife;

import static apps.android.kizema.medconfreminder.auth.helpers.ImageConstatnts.REQUEST_CODE_GALLERY;

public class MainActivity extends BaseActivity {

    @BindView(R.id.pager)
    public ViewPager pager;

    @BindView(R.id.ptTabStrip)
    public PagerTabStrip ptTabStrip;

    private ViewPagerAdapter viewPagerAdapter;


    public static Intent getIntent(Activity activity){
        Intent intent = new Intent(activity, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //set DB with demo data
        DBSetter.setDemoDb();

        init();
    }


    private void init() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this, getAdapterData());
        pager.setAdapter(viewPagerAdapter);
    }

    private List<DataHolder> getAdapterData() {
        DataHolder profileFrag = new DataHolder(ProfileFragment.class.getName(), null, "Profile");
        DataHolder confFrag = new DataHolder(ConferencesFragment.class.getName(), null, "Conferences");
        DataHolder inviteFrag = new DataHolder(InvitesFragment.class.getName(), null, "Invites");

        List<DataHolder> dataHolders = new ArrayList<>(1);
        dataHolders.add(profileFrag);
        dataHolders.add(confFrag);
        dataHolders.add(inviteFrag);

        return dataHolders;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_GALLERY:
                if (resultCode == RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    String photoPath = getPath(selectedImageUri);
                    openImageEditorActivity(photoPath);
                    return;
                }
        }

        ((ProfileFragment) viewPagerAdapter.getFragment(0)).handleOnActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void openImageEditorActivity(String photoPath) {
        if (photoPath == null || photoPath.length() == 0){
            Snackbar.make(pager, "Error while downloading", Snackbar.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, ProfilePhotoChooserActivity.class);
        intent.putExtra(ProfilePhotoChooserActivity.PATH, photoPath);

        startActivityForResult(intent, ImageConstatnts.CHOOSER_IMAGE_ACTIVITY);
    }
}
