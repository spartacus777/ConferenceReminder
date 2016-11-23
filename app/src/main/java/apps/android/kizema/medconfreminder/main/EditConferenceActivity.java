package apps.android.kizema.medconfreminder.main;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import apps.android.kizema.medconfreminder.App;
import apps.android.kizema.medconfreminder.R;
import apps.android.kizema.medconfreminder.base.BaseActivity;
import apps.android.kizema.medconfreminder.main.adapters.TopicAdapter;
import apps.android.kizema.medconfreminder.model.Conference;
import apps.android.kizema.medconfreminder.model.ConferenceDao;
import apps.android.kizema.medconfreminder.model.ConferenceUserTable;
import apps.android.kizema.medconfreminder.model.ConferenceUserTableDao;
import apps.android.kizema.medconfreminder.model.Topic;
import apps.android.kizema.medconfreminder.model.TopicDao;
import apps.android.kizema.medconfreminder.model.User;
import apps.android.kizema.medconfreminder.util.LongGen;
import apps.android.kizema.medconfreminder.util.UserHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static apps.android.kizema.medconfreminder.model.Topic.getAllForConferenceId;

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
    private boolean isOwner = true;

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
            isOwner = false;
        }
    }

    public List<Topic> getFromDb(){
        return getAllForConferenceId(conference.getId());
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
                if (resultCode == RESULT_OK) {
                    String topicName = data.getStringExtra("TOPIC");
                    onTopicAdded(topicName);
                }
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
            deleteConference();
        }

        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    private void showCreateEventDialog(final String topicName){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create Calendar event?");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                sendShareCalendarIntent(topicName);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void sendShareCalendarIntent(String topicName){
        String date = conference.getDate();
        Calendar cal = Calendar.getInstance();

        try {
            Date mDate = dateFormatter.parse(date);
            cal.setTime(mDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, conference.getConferenceName());
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.getTimeInMillis() + 3*60*60*1000);
        intent.putExtra(CalendarContract.Events.ALL_DAY, false);// periodicity
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "You are lector on " + conference.getConferenceName() + " with topic " + topicName);
        startActivity(intent);
    }

    private void onTopicAdded(String topicName){
        topicAdapter.update(getFromDb());
        check();

        showCreateEventDialog(topicName);
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

    private void deleteConference(){

        showProgress();

        ConferenceUserTableDao dao0 = App.getDaoSession().getConferenceUserTableDao();
        List<ConferenceUserTable> conferenceUserTables = ConferenceUserTable.getAllUsersForConference(conference.getId());
        for (ConferenceUserTable c : conferenceUserTables){
            User user = User.findById(c.getUserId());
            if (user != null){
                user.getConferenceIds().remove(c);
            }

            Conference conf = Conference.findById(c.getConferneceId());
            if (conf != null){
                conf.getInvitedDoctors().remove(c);
            }

            dao0.delete(c);
        }

        TopicDao topicDao = App.getDaoSession().getTopicDao();
        List<Topic> topics = Topic.getAllForConferenceId(conference.getId());
        for (Topic t : topics){
            topicDao.delete(t);
        }

        ConferenceDao dao = App.getDaoSession().getConferenceDao();
        dao.delete(conference);

        setResult(RESULT_OK);
        finish();
        overridePendingTransition(R.anim.tab_activity_transition_in, R.anim.tab_activity_transition_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isOwner) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.edit_conf, menu);
            return true;
        }

        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteConference();
                break;
        }
        return true;
    }


}
