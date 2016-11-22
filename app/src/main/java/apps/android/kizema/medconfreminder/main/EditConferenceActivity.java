package apps.android.kizema.medconfreminder.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

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
    private static final int REQ_CODE = 32;

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

    private TopicAdapter topicAdapter;

    private Conference conference = null;

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
            if (cofId != null && cofId.length() > 0)
            conference = Conference.findById(cofId);
        }

        if (conference == null){
            conference = new Conference();
            conference.setId(LongGen.generate());
            conference.setConferenceId(UserHelper.generateConfId());
            conference.setConferenceName("");
            conference.setUserId(UserHelper.getMyUser().getUserId());
        }

        getSupportActionBar().setTitle(conference.getConferenceName());

        topicAdapter = new TopicAdapter(getFromDb());
        rvNames.setAdapter(topicAdapter);
        LinearLayoutManager mChatLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvNames.setLayoutManager(mChatLayoutManager);
        rvNames.setHasFixedSize(true);

        check();

        if (UserHelper.getMyUser().getIsAdmin()){
            btnAdd.setVisibility(View.GONE);
        }

        //disable for non-owners
        if (!UserHelper.getMyUser().getIsAdmin() || !conference.getUserId().equals(UserHelper.getMyUser().getUserId())){
            tvSave.setVisibility(View.GONE);
            etLocation.setVisibility(View.GONE);
            tvDate.setEnabled(false);
        }
    }

    public List<Topic> getFromDb(){
        return Topic.getAllForConferenceId(conference.getId());
    }

    @OnClick(R.id.btnAdd)
    public void onAddClick() {
        //create new topic
        startActivityForResult(TopicEditActivity.getIntent(this, conference.getId()), REQ_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQ_CODE:
                onTopicAdded();
                return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.tvSave)
    public void onSaveClick() {
        //save date and location

        ConferenceDao dao = App.getDaoSession().getConferenceDao();
        dao.update(conference);
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
