package apps.android.kizema.medconfreminder.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import apps.android.kizema.medconfreminder.R;

public class RegisterNameActivity extends AppCompatActivity {

    private static final String IS_ADMIN = "isAdmin";
    private static final String PASS = "pass";
    private static final String EMAIL = "email";

    private String name, email, pass;
    private boolean isAdmin;

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

        isAdmin = getIntent().getBooleanExtra(IS_ADMIN, false);
        pass = getIntent().getStringExtra(PASS);
        email = getIntent().getStringExtra(EMAIL);
    }
}
