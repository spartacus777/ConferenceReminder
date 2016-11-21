package apps.android.kizema.medconfreminder.auth.helpers;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import apps.android.kizema.medconfreminder.R;
import apps.android.kizema.medconfreminder.util.UIHelper;

/**
 * Created by A.Kizema on 22.11.2016.
 */

public class PictureGetterDialog extends Dialog {

    private TextView tvMakePhoto, tvChooseFromGallery, tvCancel, tvDelete;
    private ViewGroup abLayout;


    private PictureGetterDialogListener pictureGetterDialogListener;

    public interface PictureGetterDialogListener{
        void onCapturePhotoPressed();
        void onSelectPhotoFromGalleryPressed();
        void onDeletePressed();
    }

    public PictureGetterDialog(Context context) {
        super(context, R.style.OpacityDialog);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.picture_getter_dialog);

        init();
    }

    public void setPictureGetterDialogListener(PictureGetterDialogListener listener){
        this.pictureGetterDialogListener = listener;
    }

    private void init() {
        abLayout = (ViewGroup) findViewById(R.id.abLayout);
        abLayout.getLayoutParams().width = (int) (UIHelper.getW()*0.8f);

        tvMakePhoto = (TextView) findViewById(R.id.tvMakePhoto);
        tvMakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
                if (pictureGetterDialogListener != null){
                    pictureGetterDialogListener.onCapturePhotoPressed();
                }
            }
        });

        if (!getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            tvMakePhoto.setVisibility(View.GONE);
        }

        tvChooseFromGallery = (TextView) findViewById(R.id.tvChooseFromGallery);
        tvChooseFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
                if (pictureGetterDialogListener != null){
                    pictureGetterDialogListener.onSelectPhotoFromGalleryPressed();
                }
            }
        });


        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        tvDelete = (TextView) findViewById(R.id.tvDelete);
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pictureGetterDialogListener != null){
                    pictureGetterDialogListener.onDeletePressed();
                }

                photoRemoved();
            }
        });
    }

    public void photoAdded(){
        tvDelete.setVisibility(View.VISIBLE);
    }

    public void photoRemoved(){
        tvDelete.setVisibility(View.GONE);
    }

}
