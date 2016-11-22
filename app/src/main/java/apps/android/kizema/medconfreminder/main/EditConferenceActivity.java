package apps.android.kizema.medconfreminder.main;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import apps.android.kizema.medconfreminder.App;
import apps.android.kizema.medconfreminder.R;
import apps.android.kizema.medconfreminder.base.BaseActivity;
import apps.android.kizema.medconfreminder.main.adapters.TopicAdapter;
import apps.android.kizema.medconfreminder.model.Conference;
import apps.android.kizema.medconfreminder.model.ConferenceDao;
import apps.android.kizema.medconfreminder.model.Topic;
import apps.android.kizema.medconfreminder.util.LongGen;
import apps.android.kizema.medconfreminder.util.UserHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditConferenceActivity extends BaseActivity {

    public static final String CONF_ID = "wejkfnwehed";
    private static final int TOPID_EDIT_CODE = 32;
    private static final int INVITE_DOCS_CODE = 922;

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    @BindView(R.id.rvNames)
    RecyclerView rvNames;

    @BindView(R.id.tvNoItems)
    TextView tvNoItems;

    @BindView(R.id.btnAdd)
    View btnAdd;

    @BindView(R.id.tvTopics)
    TextView tvTopics;

    @BindView(R.id.tvSave)
    TextView tvSave;

    @BindView(R.id.tvDate)
    TextView tvDate;

    @BindView(R.id.etLocation)
    EditText etLocation;

    @BindView(R.id.etName)
    EditText etName;

    @BindView(R.id.ll0)
    View llName;

    @BindView(R.id.tvInviteDoctors)
    TextView tvInviteDoctors;

    private TopicAdapter topicAdapter;
    private Conference conference = null;
    private boolean isUpdate = false;
    private Calendar newDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_conference);
        ButterKnife.bind(this);

        init();
    }

    private void init(){
        if (getIntent() != null){
            String cofId = getIntent().getStringExtra(CONF_ID);
            if (cofId != null && cofId.length() > 0) {
                conference = Conference.findById(cofId);
                isUpdate = true;
                llName.setVisibility(View.GONE);
            }
        }

        if (conference == null){
            conference = new Conference();
            conference.setId(LongGen.generate());
            conference.setConferenceId(UserHelper.generateConfId());
            conference.setConferenceName("New Conference");
            conference.setUserId(UserHelper.getMyUser().getUserId());
            isUpdate = false;
        }

        getSupportActionBar().setTitle(conference.getConferenceName());

        topicAdapter = new TopicAdapter(getFromDb());
        rvNames.setAdapter(topicAdapter);
        LinearLayoutManager mChatLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvNames.setLayoutManager(mChatLayoutManager);
        rvNames.setHasFixedSize(true);

        check();

        tvDate.setText(conference.getDate());
        etLocation.setText(conference.getLocation());

        if (UserHelper.getMyUser().getIsAdmin()){
            btnAdd.setVisibility(View.GONE);
        }

        //disable for non-owners
        if (!UserHelper.getMyUser().getIsAdmin() || !conference.getUserId().equals(UserHelper.getMyUser().getUserId())){
            tvSave.setVisibility(View.GONE);
            etLocation.setVisibility(View.GONE);
            tvDate.setEnabled(false);
            llName.setVisibility(View.GONE);
            tvInviteDoctors.setVisibility(View.GONE);
        }
    }

    public List<Topic> getFromDb(){
        return Topic.getAllForConferenceId(conference.getId());
    }

    @OnClick(R.id.btnAdd)
    public void onAddClick() {
        startActivityForResult(TopicEditActivity.getIntent(this, conference.getId()), TOPID_EDIT_CODE);
    }

    @OnClick(R.id.tvInviteDoctors)
    public void onInviteDoctors() {
        saveConference();
        startActivityForResult(InviteDocotrsActivity.getIntent(this, conference.getConferenceId()), INVITE_DOCS_CODE);
    }

    @OnClick(R.id.tvDate)
    public void onDateCLicked(){
        Calendar newCalendar = Calendar.getInstance();

        DatePickerDialog fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tvDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TOPID_EDIT_CODE:
                onTopicAdded();
                return;
            case INVITE_DOCS_CODE:
                return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void saveConference(){
        if (etName.getText() != null && etName.getText().toString().length() > 0){
            conference.setConferenceName(etName.getText().toString());
        }

        if (etLocation.getText() != null && etLocation.getText().toString().length() > 0) {
            conference.setLocation(etLocation.getText().toString());
        }

        if (newDate != null) {
            conference.setDate(dateFormatter.format(newDate.getTime()));
        }

        ConferenceDao dao = App.getDaoSession().getConferenceDao();
        Conference c = Conference.findById(conference.getConferenceId());
        if (c != null) {
            dao.update(conference);
        } else {
            dao.insert(conference);
        }
    }

    @OnClick(R.id.tvSave)
    public void onSaveClick() {
        saveConference();

        setResult(RESULT_OK);
        finish();
        overridePendingTransition(R.anim.tab_activity_transition_in, R.anim.tab_activity_transition_out);
    }

    @Override
    public void onBackPressed() {
        if (!isUpdate){
            //we delete this conference, since user did not save it
            ConferenceDao dao = App.getDaoSession().getConferenceDao();
            dao.delete(conference);
        }

        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    public void onTopicAdded(){
        topicAdapter.update(getFromDb());
        check();
    }

    private void check(){
        if (topicAdapter.getItemCount() == 0){
            tvNoItems.setVisibility(View.VISIBLE);
            rvNames.setVisibility(View.GONE);
            tvTopics.setVisibility(View.GONE);

        } else {
            tvNoItems.setVisibility(View.GONE);
            rvNames.setVisibility(View.VISIBLE);
            tvTopics.setVisibility(View.VISIBLE);
        }
    }

}
