package apps.android.kizema.medconfreminder.auth;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import apps.android.kizema.medconfreminder.base.BaseActivity;
import apps.android.kizema.medconfreminder.R;
import apps.android.kizema.medconfreminder.util.Utility;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterRoleActivity extends BaseActivity {

    @BindView(R.id.tvLogIn)
    TextView tvLogIn;

    @BindView(R.id.llDoctor)
    View llDoctor;

    @BindView(R.id.llAdmin)
    View llAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_role);
        ButterKnife.bind(this);

        inittvLoginText();
    }

    private void inittvLoginText(){
        int color = getResources().getColor(R.color.colorPrimary);

        final SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append("Already Registered?");
        sb.append(Utility.getSpannableStringWithColorValue(" Log In", color));
        tvLogIn.setText(sb);
    }

    @OnClick(R.id.tvLogIn)
    public void submit() {
        startActivity(LoginActivity.getIntent(this));
    }

    @OnClick(R.id.llDoctor)
    public void onDoctorClicked() {
        startActivity(RegisterActivity.getIntent(this, false));
    }

    @OnClick(R.id.llAdmin)
    public void onAdminClicked() {
        startActivity(RegisterActivity.getIntent(this, true));
    }
}
