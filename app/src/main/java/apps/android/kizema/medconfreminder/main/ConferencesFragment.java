package apps.android.kizema.medconfreminder.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import apps.android.kizema.medconfreminder.App;
import apps.android.kizema.medconfreminder.R;
import apps.android.kizema.medconfreminder.main.adapters.ConferenceAdapter;
import apps.android.kizema.medconfreminder.model.Conference;
import apps.android.kizema.medconfreminder.model.ConferenceDao;
import apps.android.kizema.medconfreminder.util.UserHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by A.Kizema on 22.11.2016.
 */

public class ConferencesFragment extends BaseFragment implements ConferenceAdapter.OnConferenceClickListener {

    @BindView(R.id.rvNames)
    RecyclerView rvNames;

    @BindView(R.id.tvNoItems)
    TextView tvNoItems;

    @BindView(R.id.btnAdd)
    View btnAdd;


    private ConferenceAdapter conferenceAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.conf_profile, container, false);
        ButterKnife.bind(this, view);

        init();

        return view;
    }

    private void init(){
        conferenceAdapter = new ConferenceAdapter(getFromDb(), this);
        rvNames.setAdapter(conferenceAdapter);
        LinearLayoutManager mChatLayoutManager = new LinearLayoutManager(this.getContext(),
                LinearLayoutManager.VERTICAL, false);
        rvNames.setLayoutManager(mChatLayoutManager);
        rvNames.setHasFixedSize(true);

        check();

        if (!UserHelper.getMyUser().getIsAdmin()){
            btnAdd.setVisibility(View.GONE);
        }
    }

    private void check(){
        if (conferenceAdapter.getItemCount() == 0){
            tvNoItems.setVisibility(View.VISIBLE);
            rvNames.setVisibility(View.GONE);

        } else {
            tvNoItems.setVisibility(View.GONE);
            rvNames.setVisibility(View.VISIBLE);
        }
    }

    public void onConferenceAdded(){
        conferenceAdapter.update(getFromDb());
        check();
    }

    public List<Conference> getFromDb(){
        ConferenceDao conferenceDao = App.getDaoSession().getConferenceDao();
        return conferenceDao.loadAll();
    }

    @OnClick(R.id.btnAdd)
    public void onAddClick() {
        Intent intent = new Intent(getActivity(), EditConferenceActivity.class);
        getActivity().startActivityForResult(intent, MainActivity.EDIT_CONF);
    }

    @Override
    public void onConferenceClicked(Conference c) {
        Intent intent = new Intent(getActivity(), EditConferenceActivity.class);
        intent.putExtra(EditConferenceActivity.CONF_ID, c.getConferenceId());
        getActivity().startActivityForResult(intent, MainActivity.EDIT_CONF);
    }

    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MainActivity.EDIT_CONF:
                if (resultCode == RESULT_OK) {
                    onConferenceAdded();
                }
                return;
        }
    }
}
