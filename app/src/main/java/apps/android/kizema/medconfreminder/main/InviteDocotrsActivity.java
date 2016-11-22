package apps.android.kizema.medconfreminder.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import apps.android.kizema.medconfreminder.App;
import apps.android.kizema.medconfreminder.R;
import apps.android.kizema.medconfreminder.base.BaseActivity;
import apps.android.kizema.medconfreminder.main.adapters.DoctorsAdapter;
import apps.android.kizema.medconfreminder.model.Conference;
import apps.android.kizema.medconfreminder.model.User;
import apps.android.kizema.medconfreminder.model.UserDao;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InviteDocotrsActivity extends BaseActivity implements DoctorsAdapter.OnDoctorClickListener {

    public static final String CONFERENCE = "ewfwefwefwef";

    @BindView(R.id.rvNames)
    public RecyclerView rvNames;

    @BindView(R.id.tvNoItems)
    public TextView tvNoItems;

    @BindView(R.id.tvSave)
    public TextView tvSave;

    private DoctorsAdapter doctorsAdapter;
    private String confId;
    private Conference conference;
    private List<User> doctorsInvited = new ArrayList<>();

    public static Intent getIntent(Activity activity, String conf) {
        Intent intent = new Intent(activity, InviteDocotrsActivity.class);
        intent.putExtra(CONFERENCE, conf);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_docotrs);
        ButterKnife.bind(this);

        init();
    }


    private void init() {
        getSupportActionBar().setTitle("Invite doctors");

        if (getIntent() != null) {
            confId = getIntent().getStringExtra(CONFERENCE);
            conference = Conference.findById(confId);
        }

        doctorsAdapter = new DoctorsAdapter(getFromDb(), conference.getId(), this);
        rvNames.setAdapter(doctorsAdapter);
        LinearLayoutManager mChatLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvNames.setLayoutManager(mChatLayoutManager);
        rvNames.setHasFixedSize(true);

        check();
    }

    public List<User> getFromDb() {
        return User.loadAllDoctors();
    }

    @OnClick(R.id.tvSave)
    public void onSave() {

        UserDao dao = App.getDaoSession().getUserDao();
        for (User u : doctorsInvited) {
            u.setConferneceId(conference.getId());
            dao.update(u);
        }

        setResult(RESULT_OK);
        finish();
        overridePendingTransition(R.anim.tab_activity_transition_in, R.anim.tab_activity_transition_out);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    private void check() {
        if (doctorsAdapter.getItemCount() == 0) {
            tvNoItems.setVisibility(View.VISIBLE);
            rvNames.setVisibility(View.GONE);
        } else {
            tvNoItems.setVisibility(View.GONE);
            rvNames.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onDoctorClicked(User user) {
        doctorsInvited.add(user);
    }
}
