package apps.android.kizema.medconfreminder.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import apps.android.kizema.medconfreminder.App;
import apps.android.kizema.medconfreminder.R;
import apps.android.kizema.medconfreminder.model.Conference;
import apps.android.kizema.medconfreminder.model.ConferenceDao;
import apps.android.kizema.medconfreminder.model.Topic;
import apps.android.kizema.medconfreminder.model.TopicDao;
import apps.android.kizema.medconfreminder.util.LongGen;
import apps.android.kizema.medconfreminder.util.UserHelper;
import butterknife.ButterKnife;

/**
 * Created by A.Kizema on 22.11.2016.
 */

public class InvitesFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        init();

        return view;
    }

    private void init(){

    }
}
