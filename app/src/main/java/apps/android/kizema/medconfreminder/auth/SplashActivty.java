package apps.android.kizema.medconfreminder.auth;

import android.content.Intent;
import android.os.Bundle;

import apps.android.kizema.medconfreminder.BaseActivity;
import apps.android.kizema.medconfreminder.R;

public class SplashActivty extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activty);

//        TODO check session
        startRegister();
    }

    private void startRegister(){
        Intent intent = new Intent(this, RegisterRoleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //we have valid token
    private void openApp(){

    }

}
