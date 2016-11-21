package apps.android.kizema.medconfreminder.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import apps.android.kizema.medconfreminder.R;

public class SplashActivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activty);

//        TODO check session
        startRegister();
    }

    private void startRegister(){
        Intent intent = new Intent(this, RegisterRoleActivity.class);
        startActivity(intent);
    }

    //we have valid token
    private void openApp(){

    }

}
