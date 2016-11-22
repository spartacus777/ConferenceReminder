package apps.android.kizema.medconfreminder.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.EditText;
import android.widget.TextView;

import apps.android.kizema.medconfreminder.base.BaseActivity;
import apps.android.kizema.medconfreminder.CoreService;
import apps.android.kizema.medconfreminder.main.MainActivity;
import apps.android.kizema.medconfreminder.R;
import apps.android.kizema.medconfreminder.auth.control.AuthServerApi;
import apps.android.kizema.medconfreminder.util.validator.NameValidator;
import apps.android.kizema.medconfreminder.util.validator.PasswordValidator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.tvNext)
    TextView tvNext;

    public static Intent getIntent(Activity activity){
        Intent intent = new Intent(activity, LoginActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.tvNext)
    public void onNextClicked() {

        if (!NameValidator.getInstance().validate(etEmail.getText().toString())){
            Snackbar.make(etEmail, "Email is invalid", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (!PasswordValidator.getInstance().validate(etPassword.getText().toString())){
            Snackbar.make(etEmail, "Password should be more than 6 symbols and contain at least one digit", Snackbar.LENGTH_SHORT)  .show();
            return;
        }

        showProgress();
        CoreService.getInstance().getAuthServerApi().login(etEmail.getText().toString(), etPassword.getText().toString(), new AuthServerApi.OnRegisterListener() {
            @Override
            public void onRegistered() {
                hideProgress();

                Intent intent = MainActivity.getIntent(LoginActivity.this);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onError(Object err) {
                hideProgress();
                Snackbar.make(etEmail, "Wrong credentials", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
