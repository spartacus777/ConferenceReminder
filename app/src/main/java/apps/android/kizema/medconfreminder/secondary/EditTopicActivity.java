package apps.android.kizema.medconfreminder.secondary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.EditText;
import android.widget.TextView;

import apps.android.kizema.medconfreminder.App;
import apps.android.kizema.medconfreminder.R;
import apps.android.kizema.medconfreminder.base.BaseActivity;
import apps.android.kizema.medconfreminder.model.Topic;
import apps.android.kizema.medconfreminder.model.TopicDao;
import apps.android.kizema.medconfreminder.util.UserHelper;
import apps.android.kizema.medconfreminder.util.validator.StringValidator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditTopicActivity extends BaseActivity {

    public static final String CONF_ID = "sjwCONF_ID";

    @BindView(R.id.tvSave)
    TextView tvSave;

    @BindView(R.id.etName)
    EditText etName;

    private long conferenceId = 0;

    public static Intent getIntent(Activity activity, long conferenceId){
        Intent intent = new Intent(activity, EditTopicActivity.class);
        intent.putExtra(CONF_ID, conferenceId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_edit);
        ButterKnife.bind(this);

        init();
    }

    private void init(){
        getSupportActionBar().setTitle("Create New Topic");

        if (getIntent() != null){
            conferenceId = getIntent().getLongExtra(CONF_ID, 0);
        }
    }

    @OnClick(R.id.tvSave)
    public void onSaveClick() {
        if (!StringValidator.getInstance().validate(etName.getText().toString())){
            Snackbar.make(etName, R.string.invalid_topic_name, Snackbar.LENGTH_SHORT).show();
            return;
        }

        TopicDao dao = App.getDaoSession().getTopicDao();

        Topic topic = new Topic();
        topic.setTopicId(UserHelper.generateConfId());
        topic.setUserId(UserHelper.getMyUser().getUserId());
        topic.setName(etName.getText().toString());
        topic.setConferneceId(conferenceId);
        dao.insert(topic);

        Intent intent = new Intent();
        intent.putExtra("TOPIC", topic.getName());
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.tab_activity_transition_in, R.anim.tab_activity_transition_out);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}
