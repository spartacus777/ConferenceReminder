package apps.android.kizema.medconfreminder.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.SpannableStringBuilder;
import android.widget.EditText;
import android.widget.TextView;

import apps.android.kizema.medconfreminder.base.BaseActivity;
import apps.android.kizema.medconfreminder.R;
import apps.android.kizema.medconfreminder.util.Utility;
import apps.android.kizema.medconfreminder.util.validator.EmailValidator;
import apps.android.kizema.medconfreminder.util.validator.PasswordValidator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    private static final String IS_ADMIN = "isAdmin";

    private boolean isAdmin;

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.tvNext)
    TextView tvNext;

    public static Intent getIntent(Activity activity, boolean isAdmin){
        Intent intent = new Intent(activity, RegisterActivity.class);
        intent.putExtra(IS_ADMIN, isAdmin);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        isAdmin = getIntent().getBooleanExtra(IS_ADMIN, false);
        tvNextApplyText();
    }

    private void tvNextApplyText(){
        int color = getResources().getColor(R.color.colorPrimary);

        final SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append("Click");
        sb.append(Utility.getSpannableStringWithColorValue(" NEXT", color));
        sb.append(" to register");
        tvNext.setText(sb);
    }

    @OnClick(R.id.tvNext)
    public void onNextClicked() {

        if (!EmailValidator.getInstance().validate(etEmail.getText().toString())){
            Snackbar.make(etEmail, "Email is invalid", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (!PasswordValidator.getInstance().validate(etPassword.getText().toString())){
            Snackbar.make(etEmail, R.string.invalid_pass, Snackbar.LENGTH_SHORT).show();
            return;
        }

        startActivity(RegisterNameActivity.getIntent(this, isAdmin, etPassword.getText().toString(), etEmail.getText().toString()));
    }
}
