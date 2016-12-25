package apps.android.kizema.medconfreminder.auth;

import android.content.Intent;
import android.os.Bundle;

import apps.android.kizema.medconfreminder.R;
import apps.android.kizema.medconfreminder.base.BaseActivity;
import apps.android.kizema.medconfreminder.main.MainActivity;

public class SplashActivty extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activty);

//        // check if we have session
//        if (Session.getInstance().getNameToken() != null && Session.getInstance().getNameToken().length() > 0 &&
//                Session.getInstance().getPassToken() != null && Session.getInstance().getPassToken().length() > 0 ){
//
//            if (AccountUser.find(Session.getInstance().getNameToken(), Session.getInstance().getPassToken()) != null){
//                openApp();
//                return;
//            }
//        }
//
//        startRegister();
    }

    private void startRegister(){
        Intent intent = new Intent(this, RegisterRoleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void openApp(){
        Intent intent = MainActivity.getIntent(SplashActivty.this);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
