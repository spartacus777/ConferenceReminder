package apps.android.kizema.medconfreminder.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import apps.android.kizema.medconfreminder.R;
import apps.android.kizema.medconfreminder.base.BaseFragment;
import apps.android.kizema.medconfreminder.main.adapters.InviteAdapter;
import apps.android.kizema.medconfreminder.model.Conference;
import apps.android.kizema.medconfreminder.model.ConferenceUserTable;
import apps.android.kizema.medconfreminder.secondary.EditConferenceActivity;
import apps.android.kizema.medconfreminder.util.UserHelper;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by A.Kizema on 22.11.2016.
 */

public class InvitesFragment extends BaseFragment implements InviteAdapter.OnInviteCLickListener {

    @BindView(R.id.rvNames)
    public RecyclerView rvNames;

    @BindView(R.id.tvNoInvites)
    public TextView tvNoInvites;

    private InviteAdapter inviteAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invites, container, false);
        ButterKnife.bind(this, view);

        init();
        return view;
    }

    private void init(){
        inviteAdapter = new InviteAdapter(getFromDb(), this);
        rvNames.setAdapter(inviteAdapter);
        LinearLayoutManager mChatLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvNames.setLayoutManager(mChatLayoutManager);
        rvNames.setHasFixedSize(true);

        check();
    }

    private void check(){
        if (inviteAdapter.getItemCount() == 0){
            tvNoInvites.setVisibility(View.VISIBLE);
            rvNames.setVisibility(View.GONE);
        } else {
            tvNoInvites.setVisibility(View.GONE);
            rvNames.setVisibility(View.VISIBLE);
        }
    }

    public List<Conference> getFromDb(){
        List<Conference> conferences = new ArrayList<>();
        List<ConferenceUserTable> list = UserHelper.getMyUser().getConferenceIds();
        for (ConferenceUserTable conferenceUserTable : list) {
            Conference conference = Conference.findById(conferenceUserTable.getConferneceId());
            conferences.add(conference);
        }

        return conferences;
    }

    @Override
    public void onConferenceCLicked(Conference conference) {
        Intent intent = new Intent(getActivity(), EditConferenceActivity.class);
        intent.putExtra(EditConferenceActivity.CONF_ID, conference.getConferenceId());
        getActivity().startActivity(intent);
    }

    @Override
    public void onRefresh() {
        check();
    }
}
