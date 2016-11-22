package apps.android.kizema.medconfreminder.main;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import apps.android.kizema.medconfreminder.R;

/**
 * Created by A.Kizema on 22.11.2016.
 */

public class BaseFragment extends Fragment {
    private Dialog progressDialog;
    private boolean isShownDialog = false;
    private View progress;
    private Animation anim;

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
        if (progressDialog  == null){
            initAnimation();

            progressDialog = new Dialog(getContext());
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
                    if ( getActivity() != null) {
                        getActivity().finish();
                    }
                }
                return false;
            }
        });

        if (isShownDialog) {
            return;
        }
        isShownDialog = true;

        if (android.os.Build.VERSION.SDK_INT >= 17) {
            if (getActivity() != null && !getActivity().isDestroyed()) {
                progressDialog.show();
                animateView();
            }
        } else if (getActivity() != null && !getActivity().isFinishing()) {
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
                if (getActivity() != null && !getActivity().isDestroyed()) {
                    progressDialog.dismiss();
                }
            } else if (getActivity() != null && !getActivity().isFinishing()) {
                progressDialog.dismiss();
            }
        }
    }
}
